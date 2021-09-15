package com.ewus.ba.energielenkerEneffcoService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.HttpUrl;
import okhttp3.Call;
import okhttp3.Callback;

import com.ewus.ba.energielenkerEneffcoService.model.Facility;

// TODO: telete if not necessary. Otherwise rename MS, f.i.. to dataService. wär aber schön wenns ausschließlich energielenker

public class EneffcoUtils {
  public static String tokenEneffco = "Basic "
      + Config.readProperty("src/main/resources/dbConfig.properties", "tokenEneffco");

  public static String ENEFFCO_BASE_URL = "https://ewus.eneffco.de/api/v1.0";

  private static final OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS).build();
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
   * Fetches and sets Vorlauf, Ruecklauf, Volumenstrom, Leistung und
   * Aussentemperatur ids for eneffco. requeires code, wmzEneffco and
   * aussentemperaturCode to be set
   *
   * @param c
   * @param kIobject KIobject with present code, wmzEneffco, and
   *                 aussentemperaturCode
   */
  public static void fetchEneffcoIds(Connection c, Facility facility) {
    try {
      facility.setVorlaufId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.VL." + facility.getWmzEneffco()));
      facility.setRuecklaufId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.RL." + facility.getWmzEneffco()));
      facility.setVolumenstromId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.VS." + facility.getWmzEneffco()));
      facility.setLeistungId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.L." + facility.getWmzEneffco()));
      facility.setNutzungsgradId(getEneffcoId(c, facility.getCode() + ".WEZ.ETA." + facility.getWmzEneffco()));
      facility.setAuslastungKgrId(getEneffcoId(c, facility.getCode() + ".WEZ.AUS.KGR." + facility.getWmzEneffco()));
      facility.setDeltaTemeratureId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.DT." + facility.getWmzEneffco()));
      facility.setAussentemperaturId(getEneffcoId(c, facility.getAussentemperaturCode()));

      // System.out.println("## Eneffco ids: " + facility.getCode());
      // System.out.println("VorlaufId: " + facility.getVorlaufId());
      // System.out.println("RuecklaufId: " + facility.getRuecklaufId());
      // System.out.println("VolumenstromId: " + facility.getVolumenstromId());
      // System.out.println("LeistungId: " + facility.getLeistungId());
      // System.out.println("AussentemperaturId: " +
      // facility.getAussentemperaturId());
      // System.out.println("NutzungsgradId: " + facility.getNutzungsgradId());
      // System.out.println("AuslastungKgrId: " + facility.getAuslastungKgrId());
      // System.out.println("getDeltaTemeratureId: " +
      // facility.getDeltaTemeratureId());

    } catch (Exception e) {
      // System.err.println("# Error fetchEneffcoIds: " + facility.getCode());
      Utils.LOGGER.log(Level.WARNING, "Error fetchEneffcoIds: " + facility.getCode() + e.getMessage(), e);
    }
  }

  public static String getEneffcoId(Connection connection, String code) {
    ResultSet resultSet = null;
    try {
      Statement statement = connection.createStatement();
      {
        // Create and execute a SELECT SQL statement.
        String selectSql = "  SELECT [code], [id] FROM [ewus_assets].[dbo].[eneffco_datapoint] WHERE code = '" + code
            + "'";

        resultSet = statement.executeQuery(selectSql);

        // Print results from select statement
        while (resultSet.next()) {
          // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
          System.out.println(resultSet.getString(1) + " | " + resultSet.getString(2) + " | ");
          // eSZ_Einsparungen.add(resultSet.getString(1));
          return resultSet.getString(2);

        }

      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    return "";
  }

  public static String getEneffcoRawDatapoinId(Connection connection, String code) {
    ResultSet resultSet = null;
    // System.out.println("getEneffcoRawDatapointId for: " + code);
    try {
      Statement statement = connection.createStatement();
      {
        // Create and execute a SELECT SQL statement.
        String selectSql = "  SELECT [code], [id] FROM [ewus_assets].[dbo].[eneffco_rawdatapoint] WHERE code = '" + code
            + "'";

        resultSet = statement.executeQuery(selectSql);

        // Print results from select statement
        while (resultSet.next()) {
          // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
          // System.out.println(resultSet.getString(1) + " | " + resultSet.getString(2) +
          // " | ");
          // eSZ_Einsparungen.add(resultSet.getString(1));
          return resultSet.getString(2);

        }
      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    return "";
  }

  public static String readEneffcoDatapoint(String datapointId) {
    try {
      URL url;
      url = new URL(ENEFFCO_BASE_URL + "/datapoint/" + datapointId);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.setRequestProperty("Accept", "application/json");
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Authorization", tokenEneffco);

      // read the response
      InputStream in = new BufferedInputStream(connection.getInputStream());
      String result = IOUtils.toString(in, "UTF-8");

      in.close();
      connection.disconnect();
      return result;
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      return "";
    }
  }

  // TODO: use HttpUrl.Builder und .addQueryParameter("from", from)
  public static List<JSONObject> readEneffcoDatapointValues(String datapointId, String from, String to,
      int timeInterval, boolean includeNanValues) {
    // System.out.println("readEneffcoDatapointValues: datapointId: " + datapointId
    // + ", from: " + from + ", to: " + to
    // + ", timeInterval: " + timeInterval + ", includeNanValues: " +
    // includeNanValues);

    HttpUrl.Builder httpBuilder = HttpUrl.parse(ENEFFCO_BASE_URL + "/datapoint/" + datapointId + "/value").newBuilder();
    httpBuilder.addQueryParameter("from", from);
    httpBuilder.addQueryParameter("to", to);
    httpBuilder.addQueryParameter("timeInterval", String.valueOf(timeInterval));
    httpBuilder.addQueryParameter("includeNanValues", String.valueOf(includeNanValues));

    Request request = new Request.Builder().url(httpBuilder.build()).addHeader("Authorization", tokenEneffco).build();
    Response response = null;
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 500) {
        System.out.println("Response get enfeffco datapoint values: " + response.code());
        System.out.println(response);
        return null;
      }

      String responseBody = response.body().string();
      JSONArray jArray = new JSONArray(responseBody);
      List<JSONObject> jObjects = new ArrayList<>();

      for (int i = 0; i < jArray.length(); i++) {
        jObjects.add(jArray.getJSONObject(i));
      }
      return jObjects;

    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      return null;
    }
  }
}
