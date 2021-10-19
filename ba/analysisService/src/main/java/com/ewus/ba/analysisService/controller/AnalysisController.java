package com.ewus.ba.analysisService.controller;

import com.ewus.ba.analysisService.Utils;
import com.ewus.ba.analysisService.model.Facility;
import com.ewus.ba.analysisService.model.FacilityAnalysisConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(value = "/")
@CrossOrigin
public class AnalysisController {
  private static final OkHttpClient client =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(60, TimeUnit.SECONDS)
          .build();

  final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping("/analyse")
  @ResponseBody
  public Map<String, List<String>> analyse(@RequestBody String codes) {
    List<Facility> facilities = retrieveFacilities(codes);
    List<FacilityAnalysisConfiguration> configs = retrieveConfigs(codes);

    Map<String, List<String>> textFragments = new HashMap<>();
    for (Facility facility : facilities) {
      // If no config is set, run all analyses by default
      FacilityAnalysisConfiguration config =
          configs.stream()
              .filter(c -> facility.getCode().toUpperCase().equals(c.getId().toUpperCase()))
              .findFirst()
              .orElse(
                  new FacilityAnalysisConfiguration(facility.getCode(), true, true, true, true));

      textFragments.put(facility.getCode(), facility.analayse(config));
    }

    System.out.println("Done Analysing");
    return textFragments;
  }

  private List<Facility> retrieveFacilities(String codes) {
    HttpUrl.Builder httpBuilder =
        HttpUrl.parse("http://localhost:8080/facilities-data-list").newBuilder();
    httpBuilder.addQueryParameter("codes", codes);
    Request request = new Request.Builder().url(httpBuilder.build()).build();
    Response response = null;
    List<Facility> facilities = new ArrayList<>();
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
        Utils.LOGGER.log(Level.WARNING, "Response GET /facilities-data-list: " + response);
        return null;
      }

      facilities =
          objectMapper.readValue(response.body().string(), new TypeReference<List<Facility>>() {});

    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      return null;
    }
    return facilities;
  }

  private List<FacilityAnalysisConfiguration> retrieveConfigs(String codes) {
    HttpUrl.Builder httpBuilder =
        HttpUrl.parse("http://localhost:8082/configs/get-list").newBuilder();
    httpBuilder.addQueryParameter("codes", codes);
    Request request = new Request.Builder().url(httpBuilder.build()).build();
    Response response = null;
    List<FacilityAnalysisConfiguration> configs = new ArrayList<>();
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
        Utils.LOGGER.log(Level.WARNING, "Response GET /codes/get-list: " + response);
        return null;
      }

      String body = response.body().string();
      configs =
          objectMapper.readValue(body, new TypeReference<List<FacilityAnalysisConfiguration>>() {});
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    return configs;
  }
}
