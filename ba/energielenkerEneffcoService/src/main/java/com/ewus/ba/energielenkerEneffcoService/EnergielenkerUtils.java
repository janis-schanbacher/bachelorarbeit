package com.ewus.ba.energielenkerEneffcoService;

import com.ewus.ba.energielenkerEneffcoService.model.Facility;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class EnergielenkerUtils {

  public static String tokenEnergielenker;

  public static String loginEnergielenker() {
    try {
      Properties credentials =
          Config.readProperties(
              "src/main/resources/dbConfig.properties",
              new String[] {"energielenkerUsername", "energielenkerPassword"});
      String query_url = "https://ewus.elmonitor.de/api/login_check";
      String json =
          "{ \"username\" :  \""
              + credentials.getProperty("energielenkerUsername")
              + "\" , \"password\" : \""
              + credentials.getProperty("energielenkerPassword")
              + "\" }";
      URL url = new URL(query_url);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      conn.setDoOutput(true);
      conn.setDoInput(true);

      conn.setRequestMethod("POST");
      OutputStream os = conn.getOutputStream();
      os.write(json.getBytes("UTF-8"));
      os.close();
      // read the response
      InputStream in = new BufferedInputStream(conn.getInputStream());
      String result = IOUtils.toString(in, "UTF-8");
      // System.out.println(result);
      JSONObject myResponse = new JSONObject(result);
      // System.out.println("token : " + myResponse.getString("token"));
      tokenEnergielenker = myResponse.getString("token");
      in.close();
      conn.disconnect();
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    return tokenEnergielenker;
  }

  public static String[] getStringEnergielenker(String obId, String attributeId) {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    String creationTime = "", value = "";
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/objects/"
                    + obId
                    + "/attributes/"
                    + attributeId)
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    try {
      JSONObject response = new JSONObject(client.newCall(request).execute().body().string());
      creationTime =
          response
              .getJSONArray("values")
              .getJSONObject(0)
              .getJSONObject("value")
              .getString("creationTime");

      if (attributeId.equals("2578")) {
        value =
            response
                .getJSONArray("values")
                .getJSONObject(0)
                .getJSONObject("resolvedValue")
                .getString("name");
      } else { // if (attributeId.equals("2882") || attributeId.equals("2953")) {
        value = response.getJSONArray("values").getJSONObject(0).getString("resolvedValue");
      }
    } catch (IOException e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }

    return new String[] {value, creationTime};
  }

  public static void fetchLiegenschaftFieldValues(Connection dbConnection, Facility facility) {
    // TODO: check if to delte
    facility.setAussentemperaturCode(
        EnergielenkerUtils.getStringEnergielenker(facility.getLiegenschaftObjectId(), "2882")[0]);
    facility.setVersorgungstyp(
        EnergielenkerUtils.getStringEnergielenker(facility.getLiegenschaftObjectId(), "2578")[0]);
  }

  public static void postStringEnergielenker(String obId, String attributeId, String pValue)
      throws Exception {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("{\n    \"value\": \"" + pValue + "\"\n}", mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/attributes/"
                    + attributeId
                    + "/values?entityId="
                    + obId
                    + "&sourceId=1")
            .method("PATCH", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    try {
      Response response = client.newCall(request).execute();
      if (response.code() == 400 || response.code() == 500) {
        System.out.println("Response post to Energielenker (String): " + response.code());
        System.out.println("{\n    \"value\": " + pValue + "\n}");
        System.out.println(response);
      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      throw new Exception();
    }
  }

  public static void postIntEnergielenker(String obId, String attributeId, int pValue)
      throws Exception {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("{\n    \"value\": " + pValue + "\n}", mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/attributes/"
                    + attributeId
                    + "/values?entityId="
                    + obId
                    + "&sourceId=1")
            .method("PATCH", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    try {
      Response response = client.newCall(request).execute();
      if (response.code() == 400 || response.code() == 500) {
        System.out.println("Response post to Energielenker (int): " + response.code());
        System.out.println("{\n    \"value\": " + pValue + "\n}");
        System.out.println(response);
      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
      throw new Exception();
    }
  }

  public static void postFloatEnergielenker(String obId, String attributeId, Float pValue)
      throws Exception {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("{\n    \"value\": " + pValue + "\n}", mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/attributes/"
                    + attributeId
                    + "/values?entityId="
                    + obId
                    + "&sourceId=1")
            .method("PATCH", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.code() == 400 || response.code() == 500) {
        System.out.println("Response post to Energielenker (float): " + response.code());
        System.out.println("{\n    \"value\": " + pValue + "\n}");
        System.out.println(response);
      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    if (response.code() == 400 || response.code() == 500) {
      throw new Exception();
    }
  }

  /**
   * Fetches the attributes (such as from an Energielenker Object (such as Einsparzählerprotokoll,
   * or Regelparameter_Soll-Werte).
   *
   * @param elObjId Id of Energielenker object
   * @param facilities Facilities to be filled
   * @param index
   * @return
   */
  public static ArrayList<Facility> fillEnergielenkerFields(
      String elObjId, ArrayList<Facility> facilities, int index) {
    String query_url =
        "https://ewus.elmonitor.de/api/v1/basemonitor/objects/" + elObjId + "/attributes";
    System.out.print("Entered fillEnergielenkerFields with: ");
    System.out.println(query_url);
    try {
      URL url = new URL(query_url);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      conn.setRequestProperty("Authorization", "Bearer " + tokenEnergielenker);
      conn.setDoOutput(true);
      // conn.setDoInput(true);

      conn.setRequestMethod("GET");

      // read the response
      InputStream in = new BufferedInputStream(conn.getInputStream());
      String result = IOUtils.toString(in, "UTF-8");
      // System.out.println(result);
      // System.out.println("result after Reading JSON Response");

      ArrayList<String> listdata = new ArrayList<String>();
      JSONArray jArray = new JSONArray(result);
      //

      for (int i = 0; i < jArray.length(); i++) {

        // System.out.println(jArray.get(i));
        String test01 = "" + jArray.get(i);
        JSONObject jobject = new JSONObject(test01);

        // System.out.println(jobject);

        if (jobject.get("name").toString().contains("103 Nutzungsgrad Vorwoche")) {
          facilities
              .get(index)
              .setUtilizationRatePreviousWeek(Double.parseDouble(jobject.get("id").toString()));
        }
        if (jobject.get("name").toString().contains("960 AKTUELL Textbausteine Auto Analyse")) {
          // System.out.println("textFragments: " +
          // jobject.get("id").toString());
          facilities.get(index).setTextFragments(jobject.get("id").toString());
          // retrieve last saved textFragments with timestamp of creation
          String[] textFragmentsPrev =
              getStringEnergielenker(elObjId, jobject.get("id").toString());
          facilities
              .get(index)
              .setTextFragmentsValue(textFragmentsPrev[1] + ": " + textFragmentsPrev[0]);
        }
        if (jobject.get("name").toString().contains("961 ALT Textbausteine Auto Analyse")) {
          // System.out.println("textFragmentsPrev: " +
          // jobject.get("id").toString());
          facilities.get(index).setTextFragmentsPrev(jobject.get("id").toString());
        }

        // Values
        if (jobject.get("name").toString().contains("190 WMZ Eneffco (letzte Stelle im DP Code)")) {
          facilities
              .get(index)
              .setWmzEneffco(
                  Integer.parseInt(getAttributeValue(elObjId, jobject.get("id").toString())));
        }

        if (jobject.get("name").toString().contains("960 AKTUELL Textbausteine Auto Analyse")) {
          facilities
              .get(index)
              .setWmzEneffco(
                  Integer.parseInt(getAttributeValue(elObjId, jobject.get("id").toString())));
        }
      }

      in.close();
      conn.disconnect();
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }

    return facilities;
  }

  public static ArrayList<Facility> getFacilities(Connection dbConnection, String[] codes) {
    System.out.println("Entered getFacilities");
    ArrayList<Facility> facilities =
        Stream.of(codes)
            .map(code -> new Facility(code.replace("\"", "")))
            .collect(Collectors.toCollection(ArrayList::new));

    ResultSet resultSet = null;
    try {
      Statement statement = dbConnection.createStatement();

      String codesAsSqlList = "('" + String.join("','", codes).replace("\"", "") + "')";
      String selectSql =
          "SELECT [Code], [energielenker_sortiert].[id], [energielenker_sortiert].[name], [energielenker_objects].[parentId] "
              + "FROM [ewus_assets].[dbo].[energielenker_sortiert] JOIN [ewus_assets].[dbo].[energielenker_objects] "
              + "ON [ewus_assets].[dbo].[energielenker_sortiert].[id] = [ewus_assets].[dbo].[energielenker_objects].[id] "
              + "WHERE [Code] IN "
              + codesAsSqlList
              + " AND deinstalled = 'false'";

      resultSet = statement.executeQuery(selectSql);

      // For each result check if the referenced Object is of interest. If yes, save
      // id in corresponding Facility
      while (resultSet.next()) {
        String code = resultSet.getString(1);
        String id = resultSet.getString(2);
        String name = resultSet.getString(3);
        String parentId = resultSet.getString(4);
        // System.out.println(resultSet.getString(1) + " " +
        // resultSet.getString(2) + "
        // " + resultSet.getString(3) + " "
        // + resultSet.getString(4));

        if (name.equals("Einsparzählerprotokoll")) {
          for (int i = 0; i < facilities.size(); i++) {
            if (facilities.get(i).getCode().equalsIgnoreCase(code)) {
              facilities.get(i).setEinsparzaehlerobjektid(id);
              // Liegenschaft is parent of Anlagentechnik,
              // Einsparzählerprotokoll,
              // ESZ_Einsparungen, Messtechnik
              facilities.get(i).setLiegenschaftObjectId(parentId);
              break;
            }
          }
        } else if (name.equals("Regelparameter_Soll-Werte")) {
          for (int i = 0; i < facilities.size(); i++) {
            if (facilities.get(i).getCode().equalsIgnoreCase(code)) {
              facilities.get(i).setRegelparameterSollWerteObjectId(id);
              break;
            }
          }
        }
      }
    } catch (SQLException e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    return facilities;
  }

  /**
   * Get value of attribute from Energielenker Object specified by objectId
   *
   * @param objectId Specifies the Energielenker object, which contains the attribute
   * @param attributeId Specifies the id of the attribute
   * @return Value of the attribute as json
   */
  public static String getAttributeValue(String objectId, String attributeId) {
    String attributeValue = "";
    String query_url =
        "https://ewus.elmonitor.de/api/v1/basemonitor/objects/"
            + objectId
            + "/attributes/"
            + attributeId;

    JSONObject jobject = null;
    try {
      URL url = new URL(query_url);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      conn.setRequestProperty("Authorization", "Bearer " + tokenEnergielenker);
      conn.setDoOutput(true);
      // conn.setDoInput(true);

      conn.setRequestMethod("GET");

      // read the response
      InputStream in = new BufferedInputStream(conn.getInputStream());
      String result = IOUtils.toString(in, "UTF-8");

      jobject = new JSONObject(result);
      // System.out.println(jobject.get("values"));
      JSONArray jArray = new JSONArray(jobject.get("values").toString());
      JSONObject jobjectValue = null;
      if (jArray.length() > 0) {
        jobjectValue = new JSONObject(jArray.get(0).toString());
        attributeValue = jobjectValue.get("resolvedValue").toString();
      }

      in.close();
      conn.disconnect();
    } catch (Exception e) {
      System.err.println("# Error getAttributeValue: ");
      System.err.println(query_url);
      System.err.println(jobject);
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }
    return attributeValue;
  }

  public static List<String> fetchAllFacilityCodes(Connection dbConnection) {
    List<String> facilityCodes = new LinkedList<>();
    ResultSet resultSet = null;
    try {
      // SMBUS_48AC%/1/Volume%
      Statement statement = dbConnection.createStatement();
      {
        // Create and execute a SELECT SQL statement.
        String selectSql = "SELECT [Code] FROM [ewus_assets].[dbo].[energielenker_sortiert]";

        resultSet = statement.executeQuery(selectSql);

        // Print results from select statement
        while (resultSet.next()) {
          String code = resultSet.getString(1);
          if (code.contains(".") && code.matches("[a-zA-Z]*\\.[0-9]+")) {
            facilityCodes.add(resultSet.getString(1));
          }
        }
      }
    } catch (SQLException e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }

    facilityCodes = facilityCodes.stream().distinct().sorted().collect(Collectors.toList());
    // java.util.Collections.sort(facilityCodes);
    return facilityCodes;
  }
}
