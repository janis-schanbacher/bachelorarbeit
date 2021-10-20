package com.ewus.ba.facilityService;

import com.ewus.ba.facilityService.model.Facility;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class EnergielenkerUtils {

  private static String tokenEnergielenker;
  private static final OkHttpClient client = new OkHttpClient()
    .newBuilder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build();

  public static String loginEnergielenker() {
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

      MediaType mediaType = MediaType.parse("application/json");
      RequestBody body = RequestBody.create(json, mediaType);
      Request request =
          new Request.Builder()
              .url(query_url)
              .method("POST", body)
              .addHeader("Content-Type", "application/json")
              .build();
      try {
        Response response = client.newCall(request).execute();
        if (response.code() == 401) {
          Utils.LOGGER.warn("Energielenker login failed: wrong credentials.");
        } else if (response.code() == 200) {
          tokenEnergielenker = new JSONObject(response.body().string()).getString("token");
        }
      } catch (Exception e) {
        Utils.LOGGER.warn(e.getMessage(), e);
      }

    return tokenEnergielenker;
  }

  public static String[] getStringWithCreationTimeEnergielenker(
      String objectId, String attributeId) {
    if (objectId == null || attributeId == null) {
      Utils.LOGGER.warn(
          "Entered getStringWithCreationTimeEnergielenker with arguments objectId: "
              + objectId
              + ", and attributteId: "
              + attributeId
              + ". Both arguments have to be present");
      return new String[] {"", ""};
    }
    String creationTime = "", value = "";
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/objects/"
                    + objectId
                    + "/attributes/"
                    + attributeId)
            .addHeader("accept", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        response.close();
        throw new Exception(
            "Failed to retrieve String from Energielenker. Request: "
                + request
                + ", response: "
                + response);
      }
      JSONObject responseBody = new JSONObject(response.body().string());
      JSONArray values = responseBody.getJSONArray("values");
      if (values.length() > 0) {
        creationTime = values.getJSONObject(0).getJSONObject("value").getString("creationTime");

        if (attributeId.equals("2578")) {
          value = values.getJSONObject(0).getJSONObject("resolvedValue").getString("name");
        } else { // e.g. if (attributeId.equals("2882") || attributeId.equals("2953"))
          value = values.getJSONObject(0).getString("resolvedValue");
        }
      }
    } catch (IOException e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }

    return new String[] {value, creationTime};
  }

  /**
   * Get value of attribute from Energielenker Object specified by objectId
   *
   * @param objectId Specifies the Energielenker object, which contains the attribute
   * @param attributeId Specifies the id of the attribute
   * @return Value of the attribute as json
   */
  public static String getAttributeValue(String objectId, String attributeId) {
    if (objectId == null || attributeId == null) {
      Utils.LOGGER.warn(
          "Entered getAttributeValue with arguments objectId: "
              + objectId
              + ", and attributteId: "
              + attributeId
              + ". Both arguments have to be present.");
      return "";
    }
    String attributeValue = "";
    String query_url =
        "https://ewus.elmonitor.de/api/v1/basemonitor/objects/"
            + objectId
            + "/attributes/"
            + attributeId;

    Request request = new Request.Builder()
        .url(query_url)
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer " + tokenEnergielenker)
        .build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        response.close();
        throw new Exception(
            "Failed to get Value from Energielenker. Request: " + request + ", response: " + response);
      }

      JSONObject responseBody = new JSONObject(response.body().string());
      JSONArray valuesJsonArray = new JSONArray(responseBody.get("values").toString());
      if (valuesJsonArray.length() > 0) {
        attributeValue = valuesJsonArray.getJSONObject(0).get("resolvedValue").toString();
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return attributeValue;
  }

  public static void postStringEnergielenker(String objectId, String attributeId, String value)
      throws Exception {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("{\n    \"value\": \"" + value + "\"\n}", mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/attributes/"
                    + attributeId
                    + "/values?entityId="
                    + objectId
                    + "&sourceId=1")
            .method("PATCH", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    try {
      Response response = client.newCall(request).execute();
      if (response.code() == 400 || response.code() == 500) {
        Utils.LOGGER.warn(
            "The request: "
                + request
                + " in postStringEnergielenker failed. Response: "
                + response);
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
      throw e;
    }
  }

  public static void postIntEnergielenker(String objectId, String attributeId, int value)
      throws Exception {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("{\n    \"value\": " + value + "\n}", mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/attributes/"
                    + attributeId
                    + "/values?entityId="
                    + objectId
                    + "&sourceId=1")
            .method("PATCH", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    try {
      Response response = client.newCall(request).execute();
      if (response.code() == 400 || response.code() == 500) {
        Utils.LOGGER.warn(
            "The request: " + request + " in postIntEnergielenker failed. Response: " + response);
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
      throw new Exception();
    }
  }

  public static void postFloatEnergielenker(String objectId, String attributeId, Float value)
      throws Exception {
    OkHttpClient client = new OkHttpClient().newBuilder().build();
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create("{\n    \"value\": " + value + "\n}", mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://ewus.elmonitor.de/api/v1/basemonitor/attributes/"
                    + attributeId
                    + "/values?entityId="
                    + objectId
                    + "&sourceId=1")
            .method("PATCH", body)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + tokenEnergielenker)
            .build();
    Response response = null;
    try {
      response = client.newCall(request).execute();
      if (response.code() == 400 || response.code() == 500) {
        Utils.LOGGER.warn(
            "The request: " + request + " in postFloatEnergielenker failed. Response: " + response);
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    if (response.code() == 400 || response.code() == 500) {
      throw new Exception();
    }
  }

  public static List<String> fetchAllFacilityCodes(Connection dbConnection) {
    List<String> facilityCodes = new ArrayList<>();
    ResultSet resultSet = null;
    try {
      Statement statement = dbConnection.createStatement();
      String selectSql = "SELECT [code] FROM [energielenker_sortiert]";
      resultSet = statement.executeQuery(selectSql);

      while (resultSet.next()) {
        String code = resultSet.getString(1);
        if (code.contains(".") && code.matches("[a-zA-Z]*\\.[0-9]+")) {
          facilityCodes.add(resultSet.getString(1));
        }
      }
    } catch (SQLException e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }

    facilityCodes = facilityCodes.stream().distinct().sorted().collect(Collectors.toList());
    return facilityCodes;
  }

  /**
   * Retrieve Energielenker-Object-Ids for each of the facilities Liegenschaft-,
   * Einsparzählerprotokoll- and Regelparameter_Soll_Werte Objects.
   *
   * @param dbConnection Connection for the central Database
   * @param facilities Facilities to be filled
   * @return the provided Facilities with set LiegenschaftObjectId, EinsparzaehlerObjectId and
   *     RegelparameterSollWerteObjectId.
   */
  public static ArrayList<Facility> fillEnergielenkerObjectIds(
      Connection dbConnection, ArrayList<Facility> facilities) {
    String[] codes = facilities.stream().map(f -> f.getCode()).toArray(String[]::new);
    ResultSet resultSet = null;
    try {
      Statement statement = dbConnection.createStatement();

      String codesAsSqlList = "('" + String.join("','", codes).replace("\"", "") + "')";
      String selectSql =
          "SELECT [energielenker_sortiert].[code], [energielenker_sortiert].[name], [energielenker_sortiert].[id], [energielenker_objects].[parentId] "
              + "FROM [energielenker_sortiert] JOIN [energielenker_objects] "
              + "ON [energielenker_sortiert].[id] = [energielenker_objects].[id] "
              + "WHERE [code] IN "
              + codesAsSqlList
              + " AND deinstalled = 'false'"
              + " AND [energielenker_objects].[name] in ('Einsparzählerprotokoll', 'Anlagentechnik', 'Regelparameter_Soll-Werte')";

      resultSet = statement.executeQuery(selectSql);

      while (resultSet.next()) {
        String code = resultSet.getString(1);
        String name = resultSet.getString(2);
        String id = resultSet.getString(3);
        String parentId = resultSet.getString(4);

        if (name.equals("Einsparzählerprotokoll")) {
          for (int i = 0; i < facilities.size(); i++) {
            if (facilities.get(i).getCode().equalsIgnoreCase(code)) {
              facilities.get(i).setEinsparzaehlerObjectId(id);
              // Liegenschaft is parent of
              facilities.get(i).setLiegenschaftObjectId(parentId);
              break;
            }
          }
        } else if (name.equals("Anlagentechnik")) {
          for (int i = 0; i < facilities.size(); i++) {
            if (facilities.get(i).getCode().equalsIgnoreCase(code)) {
              facilities.get(i).setAnlagentechnikObjectId(id);
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
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return facilities;
  }

  public static void fetchLiegenschaftFieldValues(Connection dbConnection, Facility facility) {
    facility.setAussentemperaturCode(
        EnergielenkerUtils.getStringWithCreationTimeEnergielenker(
            facility.getLiegenschaftObjectId(), "2882")[0]);
    facility.setVersorgungstyp(
        EnergielenkerUtils.getStringWithCreationTimeEnergielenker(
            facility.getLiegenschaftObjectId(), "2578")[0]);
  }

  /**
   * Fetches the attributes (such as from an Energielenker Object (such as Einsparzählerprotokoll,
   * or Regelparameter_Soll-Werte).
   *
   * @param objectId Id of Energielenker object
   * @param facilities Facilities to be filled
   * @param index
   * @return
   */
  public static Facility fillEnergielenkerFields(String objectId, Facility facility) {
    if (objectId == null || objectId == "") {
      Utils.LOGGER.warn(
          "Entered fillEnergielenkerFields with missing objectId, objectId is required.");
      return facility;
    }
    String query_url =
        "https://ewus.elmonitor.de/api/v1/basemonitor/objects/" + objectId + "/attributes";

    Request request =
    new Request.Builder()
        .url(query_url)
        .addHeader("accept", "application/json")
        .addHeader("Authorization", "Bearer " + tokenEnergielenker)
        .build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        response.close();
        throw new Exception(
            "Failed to retrieve Attributes from Energielenker. Request: " + request + ", response: " + response);
      }

      // read the response
      JSONArray responseBody = new JSONArray(response.body().string());
      for (int i = 0; i < responseBody.length(); i++) {
        JSONObject attribute = responseBody.getJSONObject(i);

        if (attribute.getString("name").contains("011 Brennwertkessel 1")) {
          facility.setBrennwertkessel(
              Boolean.parseBoolean(getAttributeValue(objectId, attribute.get("id").toString())));
        }

        if (attribute.getString("name").contains("103 Nutzungsgrad Vorwoche")) {
          facility.setUtilizationRatePreviousWeek(Double.parseDouble(attribute.get("id").toString()));
        }

        if (attribute.getString("name").contains("960 AKTUELL Textbausteine Auto Analyse")) {
          facility.setTextFragmentsId(attribute.get("id").toString());
          // retrieve last saved textFragments with timestamp of creation
          String[] textFragmentsPrevAndCreationTime =
              EnergielenkerUtils.getStringWithCreationTimeEnergielenker(
                  objectId, attribute.get("id").toString());
          facility.setTextFragments(
              textFragmentsPrevAndCreationTime[1] + ": " + textFragmentsPrevAndCreationTime[0]);
        }
        if (attribute.getString("name").contains("961 ALT Textbausteine Auto Analyse")) {
          facility.setTextFragmentsPrevId(attribute.get("id").toString());
        }

        if (attribute.getString("name").contains("190 WMZ Eneffco (letzte Stelle im DP Code)")) {
          facility.setWmzEneffco(
              Integer.parseInt(
                  EnergielenkerUtils.getAttributeValue(objectId, attribute.get("id").toString())));
        }
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return facility;
  }
}
