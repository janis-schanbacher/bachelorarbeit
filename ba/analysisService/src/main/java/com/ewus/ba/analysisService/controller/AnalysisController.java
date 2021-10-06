package com.ewus.ba.analysisService.controller;

import com.ewus.ba.analysisService.Utils;
import com.ewus.ba.analysisService.model.EneffcoValue;
import com.ewus.ba.analysisService.model.Facility;
import com.ewus.ba.analysisService.model.FacilityAnalysisConfiguration;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(value = "/")
@CrossOrigin
public class AnalysisController {

  static final int TIMEINTERVAL_15M = 900;

  static final int TIMEINTERVAL_DAY = 86400;

  static final int TIMEINTERVAL_WEEK = 86407;

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
    System.out.println("entered analyse");
    System.out.println("codes:" + codes);
    // List<Facility> facilities = fillFacilities(codes); as request
    // TODO: use eureka url
    HttpUrl.Builder httpBuilder =
        HttpUrl.parse("http://localhost:8080/facilities-data-list").newBuilder();
    httpBuilder.addQueryParameter("codes", codes);
    Request request = new Request.Builder().url(httpBuilder.build()).build();
    Response response = null;
    List<Facility> facilities = new ArrayList<>();
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
        // TODO: proper logging
        System.out.println("Response GET /facilities-data-list: " + response.code());
        System.out.println(response);
        // TODO:""
        return null;
      }

      System.out.println("response.code(): " + response.code());
      // System.out.println(response.body().string());
      facilities =
          objectMapper.readValue(response.body().string(), new TypeReference<List<Facility>>() {});

    } catch (Exception e) {
      System.out.println(facilities);
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      return null;
    }
    System.out.println("filled facilities. Size: " + facilities.size());

    // get facility configs using codes
    // use in for loop
    // TODO: save config in facility objects
    // TODO: use eureka url
    httpBuilder = HttpUrl.parse("http://localhost:8082/configs/get-list").newBuilder();
    httpBuilder.addQueryParameter("codes", codes);
    request = new Request.Builder().url(httpBuilder.build()).build();
    response = null;
    List<FacilityAnalysisConfiguration> configs = new ArrayList<>();
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
        // TODO: proper logging
        System.out.println("Response /codes/get-list: " + response.code());
        System.out.println(response);
        return null;
      }

      String body = response.body().string();
      configs =
          objectMapper.readValue(body, new TypeReference<List<FacilityAnalysisConfiguration>>() {});
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    System.out.println("Retrieved configs. Size: " + configs.size());

    Map<String, List<String>> textFragments = new HashMap<>();
    for (Facility facility : facilities) {
      System.out.println("Analysing: " + facility.getCode());
      List<String> textFragmentsCurrent = new ArrayList<>();

      System.out.println(facility.getCode());

      // TODO: !! not always use default
      // If no config availabe default to run all analyses is used
      // FacilityAnalysisConfiguration config = configs.stream().filter(c ->
      // facility.getCode().equals(c.getId()))
      // .findFirst().orElse(new FacilityAnalysisConfiguration(facility.getCode(),
      // true, true, true, true));
      FacilityAnalysisConfiguration config =
          new FacilityAnalysisConfiguration(facility.getCode(), true, true, true, true);

      // TODO: fetch configurations and only do desired analysis
      // ResponseEntity<FacilityAnalysisConfiguration> configResponse =
      // FacilityAnalysisConfigurationController
      // .getFacilityAnalysisConfiguration(facility.getCode());
      // FacilityAnalysisConfiguration config = configResponse.getBody();
      // if (configResponse.getStatusCode().value() == 204) { // TODO: falls nochmal
      // umgeschrieben: check if config
      // // == null
      // System.out.println(
      // "No config for " + facility.getCode() + " available. Using default to run
      // all
      // analyses.");
      // config = new FacilityAnalysisConfiguration(facility.getCode(), true, true,
      // true, true);
      // }

      if (config.getFacilitySize()) {
        textFragmentsCurrent.add(analyseFacilitySize(facility));
      }
      if (config.getUtilizationRate()) {
        textFragmentsCurrent.add(analyseUtilizationRate(facility));
      }
      if (config.getDeltaTemperature()) {
        textFragmentsCurrent.add(analyseDeltaTemperature(facility));
      }
      if (config.getReturnTemperature()) {
        textFragmentsCurrent.add(analyseReturnTemperature(facility));
      }

      textFragmentsCurrent.removeAll(Arrays.asList("", null));

      // Add previous textfragments
      textFragmentsCurrent.add("prev: " + facility.getTextFragments());

      textFragments.put(facility.getCode(), textFragmentsCurrent);
      System.out.println("Done Analysing " + facility.getCode());
    }

    return textFragments;
  }

  // TODO: move to analysis service
  // TODO: Bestimmung From, To
  // TODO: Move analyses to Analysis Class, not to be done in controller
  public String analyseFacilitySize(Facility facility) {
    System.out.println(
        "entered analyseFacilitySize. Code: "
            + facility.getCode()
            + " AuslastungKgrId: "
            + facility.getAuslastungKgrId());
    List<EneffcoValue> values =
        getEneffcoValues(
            facility.getAuslastungKgrId(),
            // TODO: change amount of days
            java.time.Clock.systemUTC()
                .instant()
                .truncatedTo(ChronoUnit.MILLIS)
                .minus(365, ChronoUnit.DAYS)
                .toString(),
            java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString(),
            TIMEINTERVAL_15M,
            false);

    if (values == null) {
      System.out.println("Could not retrieve Eneffco values for AuslastungKgr");
      return "";
    }

    float avgAuslastungKgr = getAverageValue(values);
    // TODO: fetch grenzwert and TextFragement from db
    String textFragment =
        avgAuslastungKgr > 80
            ? ""
            : "Heizkessel ist überdimensioniert, Durchschnittliche Auslastung "
                + avgAuslastungKgr
                + "%. Mögliche Maßnahmen: Brenner einstellen, andere Düse verbauen (geringere Heizleistung) oder Neubau.";
    System.out.println("Avg. Nutzungsgrad zw. -10 und -14 Grad: " + getAverageValue(values));
    // System.out.println(textFragment);
    System.out.println("finisehd analyseFacilitySize");
    return textFragment;
  }

  // TODO: move to analysis service
  public String analyseUtilizationRate(Facility facility) {
    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();
    int currentDay = LocalDate.now().getDayOfMonth();
    String from, to;
    if (currentMonth <= 3) { // First months of year
      from =
          LocalDateTime.of(currentYear - 1, Month.NOVEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
    } else if (currentMonth >= 11 && currentDay > 14) { // at least 2 weeks in timesspan
      from =
          LocalDateTime.of(currentYear, Month.NOVEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();

    } else {
      from =
          LocalDateTime.of(currentYear - 1, Month.NOVEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to =
          LocalDateTime.of(currentYear, Month.MARCH, 28, 23, 59, 59)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
    }

    List<EneffcoValue> values =
        getEneffcoValues(facility.getNutzungsgradId(), from, to, TIMEINTERVAL_DAY, false);

    if (values == null) {
      return "";
    }
    float avgUtilizationRate = getAverageValue(values);

    String textFragment = "";

    // TODO: choose which condition first
    // werden soll in textbaustein. E.g. Avg Nutzungsgrad vorwoche
    final double LIMIT_UTILIZATION_RATE = facility.getBrennwertkessel() ? 90 : 80;
    if (avgUtilizationRate < LIMIT_UTILIZATION_RATE) {
      textFragment =
          "Die Anlage weist einen geringen Nutzungsgrad auf (Avg. Nutzungsgrad: "
              + avgUtilizationRate
              + "%). Maßnahmen: Prüfen ob WMZ Gesamt gemessen wird, Anlagenanalyse durchführen.​";
    } else if (facility.getUtilizationRatePreviousWeek() != 0
        && facility.getUtilizationRatePreviousWeek() < LIMIT_UTILIZATION_RATE) {
      textFragment =
          "Die Anlage weist einen geringen Nutzungsgrad auf (Nutzungsgrad Vorwoche: "
              + facility.getUtilizationRatePreviousWeek()
              + "). Maßnahmen: Prüfen ob WMZ Gesamt gemessen wird, Anlagenanalyse durchführen.";
    }

    // TODO: fetch grenzwert and TextFragement from db

    System.out.println(
        "avgUtilizationRate Eneffco: "
            + avgUtilizationRate
            + ", Utitilization Rate prev week EL:"
            + facility.getUtilizationRatePreviousWeek());
    // System.out.println(textFragment);
    return textFragment;
  }

  // TODO: move to analysis service
  // TODO: Bestimmung From, To analog zu Nutzungsgrad, aber Dez bis Feb.
  public String analyseDeltaTemperature(Facility facility) {
    System.out.println("Entered analyseDeltaTemperature");
    int currentYear = LocalDate.now().getYear();
    String from =
        LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0)
            .atZone(ZoneId.of("Europe/Berlin"))
            .toInstant()
            .toString();
    String to =
        LocalDateTime.of(currentYear, Month.FEBRUARY, 28, 23, 59, 59)
            .atZone(ZoneId.of("Europe/Berlin"))
            .toInstant()
            .toString();

    List<EneffcoValue> values =
        getEneffcoValues(facility.getDeltaTemperatureId(), from, to, TIMEINTERVAL_15M, false);

    if (values == null) {
      return "";
    }
    float avgDeltaTemperature = getAverageValue(values);
    System.out.println("Avg. Temperaturdifferenz: " + getAverageValue(values));
    String textFragment = "";
    // TODO: fetch grenzwerte and TextFragements from db
    if (!facility.getTww()) { // TODO: Check. hat brennwertkessel
      if (avgDeltaTemperature < 15) {
        // TODO Die Temperaturspreizung ist mit/um xK zu gering. Fragen ob
        // entsprechend
        // ändern
        textFragment =
            "Die Temperaturspreizung ist mit "
                + avgDeltaTemperature
                + "K zu gering. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
      }
    } else {
      if (avgDeltaTemperature < 10) {
        // TODO Die Temperaturspreizung ist mit/um xK zu gering. Fragen ob
        // entsprechend
        // ändern
        textFragment =
            "Die Temperaturspreizung ist zu gering. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
      }
    }

    // System.out.println(textFragment);
    return textFragment;
  }

  // TODO: move to analysis service
  public String analyseReturnTemperature(Facility facility) {
    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();
    int currentDay = LocalDate.now().getDayOfMonth();
    String from, to;
    if (currentMonth <= 2) { // First months of year
      from =
          LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
    } else if (currentMonth >= 12 && currentDay > 14) { // at least 2 weeks in timesspan
      from =
          LocalDateTime.of(currentYear, Month.DECEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();

    } else {
      from =
          LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to =
          LocalDateTime.of(currentYear, Month.FEBRUARY, 28, 23, 59, 59)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
    }

    List<EneffcoValue> values =
        getEneffcoValues(facility.getRuecklaufId(), from, to, TIMEINTERVAL_DAY, false);

    if (values == null) {
      return "";
    }
    // TODO: fetch grenzwert and TextFragement from db
    final double LIMIT_PORTION_ACCEPTED_MIN = 95.0 / 100;
    final double LIMIT_RETURN_TEMPERATURE = 55;
    double portionOfValuesMarginBelowLimit =
        getPortionOfValuesMargin(values, LIMIT_RETURN_TEMPERATURE);

    String textFragment = "";
    // TODO: check 90% vs 95%/. Absichern, dass gut: alle werte von .RL.WMZ
    // betrachten, zählen wenn unter 55, prozentsatz bilden.Wenn Kein
    // Brennwertkessel kein Textbaustein
    if (portionOfValuesMarginBelowLimit < LIMIT_PORTION_ACCEPTED_MIN) {
      textFragment =
          "Brennwerteffekt wird nicht ausreichend genutzt. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
    }

    System.out.println(
        "portionOfValuesMarginBelowLimit Eneffco: " + portionOfValuesMarginBelowLimit);
    // System.out.println(textFragment);
    return textFragment;
  }

  public List<EneffcoValue> getEneffcoValues(
      String datapointId, String from, String to, int timeInterval, boolean includeNanValues) {
    // System.out.println("readEneffcoDatapointValues: datapointId: " + datapointId
    // + ", from: " + from + ", to: " + to
    // + ", timeInterval: " + timeInterval + ", includeNanValues: " +
    // includeNanValues);

    String baseUrl = "http://localhost:8080/eneffco/datapoint/";
    HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + datapointId + "/values").newBuilder();
    httpBuilder.addQueryParameter("from", from);
    httpBuilder.addQueryParameter("to", to);
    httpBuilder.addQueryParameter("timeInterval", String.valueOf(timeInterval));
    httpBuilder.addQueryParameter("includeNanValues", String.valueOf(includeNanValues));

    Request request = new Request.Builder().url(httpBuilder.build()).build();
    Response response = null;
    String responseBody = null;
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
        System.out.println(
            "Response get " + baseUrl + datapointId + "/values : " + response.code());
        return null;
      }

      responseBody = response.body().string();
      JSONArray jArray = new JSONArray(responseBody);
      List<EneffcoValue> values = new ArrayList<>();

      for (int i = 0; i < jArray.length(); i++) {
        values.add(objectMapper.readValue(jArray.get(i).toString(), EneffcoValue.class));
      }
      return values;

    } catch (Exception e) {
      System.out.println("Response getEneffcoValue: " + responseBody);
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      return null;
    }
  }

  // TODO move
  public float getAverageValue(List<EneffcoValue> values) {
    float sum = 0;
    for (int i = 0; i < values.size(); i++) {
      sum += values.get(i).getValue();
    }
    return sum / values.size();
  }

  public double getPortionOfValuesMargin(List<EneffcoValue> values, double limit) {
    int acceptedValuesCount = 0;
    for (int i = 0; i < values.size(); i++) {
      acceptedValuesCount += (values.get(i).getValue() < limit) ? 1 : 0;
    }
    return (double) acceptedValuesCount / values.size();
  }
}
