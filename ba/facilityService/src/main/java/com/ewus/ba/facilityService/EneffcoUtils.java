package com.ewus.ba.facilityService;

import com.ewus.ba.facilityService.model.EneffcoValue;
import com.ewus.ba.facilityService.model.Facility;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;

public class EneffcoUtils {

  public static String tokenEneffco =
      "Basic " + Config.readProperty("src/main/resources/dbConfig.properties", "eneffcoToken");

  public static String ENEFFCO_BASE_URL = "https://ewus.eneffco.de/api/v1.0";

  static final ObjectMapper objectMapper = new ObjectMapper();

  private static final OkHttpClient client =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(30, TimeUnit.SECONDS)
          .build();

  public static void printEneffcoToken(String username, String password) {
    try {
      tokenEneffco =
          "Basic "
              + Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"));
      System.out.println(tokenEneffco);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Fetches and sets Vorlauf, Ruecklauf, Volumenstrom, Leistung und Aussentemperatur ids for
   * eneffco. requeires code, wmzEneffco and aussentemperaturCode to be set
   *
   * @param c
   * @param facility Facility with present code, wmzEneffco, and aussentemperaturCode
   */
  public static void fetchEneffcoIds(Connection c, Facility facility) {
    try {
      facility.setVorlaufId(
          getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.VL." + facility.getWmzEneffco()));
      facility.setRuecklaufId(
          getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.RL." + facility.getWmzEneffco()));
      facility.setVolumenstromId(
          getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.VS." + facility.getWmzEneffco()));
      facility.setLeistungId(
          getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.L." + facility.getWmzEneffco()));
      facility.setNutzungsgradId(
          getEneffcoId(c, facility.getCode() + ".WEZ.ETA." + facility.getWmzEneffco()));
      facility.setAuslastungKgrId(
          getEneffcoId(c, facility.getCode() + ".WEZ.AUS.MAX." + facility.getWmzEneffco()));
      facility.setDeltaTemperatureId(
          getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.DT." + facility.getWmzEneffco()));

    } catch (Exception e) {
      Utils.LOGGER.warn("Error fetchEneffcoIds: " + facility.getCode() + e.getMessage(), e);
    }
  }

  public static String getEneffcoId(Connection connection, String code) {
    ResultSet resultSet = null;
    try {
      Statement statement = connection.createStatement();
      {
        String selectSql =
            "  SELECT [code], [id] FROM [ewus_assets].[dbo].[eneffco_datapoint] WHERE code = '"
                + code
                + "'";

        resultSet = statement.executeQuery(selectSql);
        while (resultSet.next()) {
          return resultSet.getString(2);
        }
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return "";
  }

  public static List<EneffcoValue> readEneffcoDatapointValues(
      String datapointId, String from, String to, int timeInterval, boolean includeNanValues) {
    HttpUrl.Builder httpBuilder =
        HttpUrl.parse(ENEFFCO_BASE_URL + "/datapoint/" + datapointId + "/value").newBuilder();
    httpBuilder.addQueryParameter("from", from);
    httpBuilder.addQueryParameter("to", to);
    httpBuilder.addQueryParameter("timeInterval", String.valueOf(timeInterval));
    httpBuilder.addQueryParameter("includeNanValues", String.valueOf(includeNanValues));

    Request request =
        new Request.Builder()
            .url(httpBuilder.build())
            .addHeader("Authorization", tokenEneffco)
            .build();
    Response response = null;
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 500) {
        Utils.LOGGER.warn(
            "The request: "
                + request
                + " in readEneffcoDatapointValues failed. Response: "
                + response);
        return null;
      }

      String responseBody = response.body().string();
      JSONArray jArray = new JSONArray(responseBody);
      List<EneffcoValue> values = new ArrayList<>();

      for (int i = 0; i < jArray.length(); i++) {
        String valueJsonWithCamelCaseKeys =
            jArray
                .getJSONObject(i)
                .toString()
                .replace("Value", "value")
                .replace("From", "from")
                .replace("To", "to");
        values.add(objectMapper.readValue(valueJsonWithCamelCaseKeys, EneffcoValue.class));
      }
      return values;

    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
      return null;
    }
  }
}
