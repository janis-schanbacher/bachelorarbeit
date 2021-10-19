package com.ewus.ba.facilityService;

import com.ewus.ba.facilityService.model.EneffcoValue;
import com.ewus.ba.facilityService.model.Facility;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;

// TODO: telete if not necessary. Otherwise rename MS, f.i.. to dataService. wär aber schön wenns
// ausschließlich energielenker

public class EneffcoUtils {

  public static String tokenEneffco =
      "Basic " + Config.readProperty("src/main/resources/dbConfig.properties", "tokenEneffco");

  public static String ENEFFCO_BASE_URL = "https://ewus.eneffco.de/api/v1.0";

  static final ObjectMapper objectMapper = new ObjectMapper();

  private static final OkHttpClient client =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(30, TimeUnit.SECONDS)
          .build();

  // public static void setEneffcoToken() {
  // tokenEneffco = "Basic " + Config.readProperty("config.properties",
  // "tokenEneffco");
  // // Alternatively generate using eneffco credentials
  // // try {
  // // tokenEneffco = "Basic " + Base64.getEncoder()
  // // .encodeToString(("username:password").getBytes("UTF-8"));
  // // System.out.println(tokenEneffco);
  // // } catch (Exception e) {
  // // System.out.println(e);
  // // }
  // }

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
        // Create and execute a SELECT SQL statement.
        String selectSql =
            "  SELECT [code], [id] FROM [ewus_assets].[dbo].[eneffco_datapoint] WHERE code = '"
                + code
                + "'";

        resultSet = statement.executeQuery(selectSql);

        // Print results from select statement
        while (resultSet.next()) {
          // System.out.println(resultSet.getString(2) + " " +
          // resultSet.getString(3));
          System.out.println(resultSet.getString(1) + " | " + resultSet.getString(2) + " | ");
          // eSZ_Einsparungen.add(resultSet.getString(1));
          return resultSet.getString(2);
        }
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return "";
  }

  // TODO: use HttpUrl.Builder und .addQueryParameter("from", from)
  public static List<EneffcoValue> readEneffcoDatapointValues(
      String datapointId, String from, String to, int timeInterval, boolean includeNanValues) {
    // System.out.println("readEneffcoDatapointValues: datapointId: " + datapointId
    // + ", from: " + from + ", to: " + to
    // + ", timeInterval: " + timeInterval + ", includeNanValues: " +
    // includeNanValues);

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
        System.out.println("Response get enfeffco datapoint values: " + response.code());
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
