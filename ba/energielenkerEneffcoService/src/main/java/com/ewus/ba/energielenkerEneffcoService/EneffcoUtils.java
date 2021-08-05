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
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.ewus.ba.energielenkerEneffcoService.model.Facility;

// TODO: telete if not necessary. Otherwise rename MS, f.i.. to dataService. wär aber schön wenns ausschließlich energielenker

public class EneffcoUtils {
    public static String tokenEneffco = "Basic "
            + Config.readProperty("src/main/resources/dbConfig.properties", "tokenEneffco");

    public static String ENEFFCO_BASE_URL = "https://ewus.eneffco.de/api/v1.0";

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
        System.out.println(
                "Fetching eneffco ids for: " + facility.getCode() + ", wmzEneffco: " + facility.getWmzEneffco());
        try {
            facility.setVorlaufId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.VL." + facility.getWmzEneffco()));
            facility.setRuecklaufId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.RL." + facility.getWmzEneffco()));
            facility.setVolumenstromId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.VS." + facility.getWmzEneffco()));
            facility.setLeistungId(getEneffcoId(c, facility.getCode() + ".WEZ.WMZ.L." + facility.getWmzEneffco()));
            facility.setNutzungsgradId(getEneffcoId(c, facility.getCode() + ".WEZ.ETA." + facility.getWmzEneffco()));
            // System.out.println("Getting aussentemperatur based on code: " +
            // facility.getAussentemperaturCode() + "...\n");
            facility.setAussentemperaturId(getEneffcoId(c, facility.getAussentemperaturCode()));
            System.out.println("## Eneffco ids: " + facility.getCode());
            System.out.println("Vorlauf: " + facility.getVorlaufId());
            System.out.println("Ruecklauf: " + facility.getRuecklaufId());
            System.out.println("Volumenstrom: " + facility.getVolumenstromId());
            System.out.println("Leistung: " + facility.getLeistungId());
            System.out.println("Aussentemperatur: " + facility.getAussentemperaturId());
            System.out.println("Nutzungsgrad: " + facility.getNutzungsgradId());

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
                String selectSql = "  SELECT [code], [id] FROM [ewus_assets].[dbo].[eneffco_datapoint] WHERE code = '"
                        + code + "'";

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
                String selectSql = "  SELECT [code], [id] FROM [ewus_assets].[dbo].[eneffco_rawdatapoint] WHERE code = '"
                        + code + "'";

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
            int statusCode = connection.getResponseCode();
            System.out.println(statusCode);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");
            System.out.println(result);

            in.close();
            connection.disconnect();
            return result;
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
            return "";
        }
    }

    // public static boolean checkIfEneffcoRawDatapointIsEmpty(String datapointId) {
    // OkHttpClient client = new OkHttpClient().newBuilder().build();

    // Request request = new Request.Builder().url(ENEFFCO_BASE_URL +
    // "/rawdatapoint/" + datapointId + "/live")
    // .addHeader("accept", "application/json").addHeader("Authorization",
    // tokenEneffco).build();
    // System.out.println("checkIfEneffcoRawDatapointIsEmpty request: " + request);
    // try {
    // JSONObject response = new
    // JSONObject(client.newCall(request).execute().body().string());
    // // System.out.println("Last update: " + response.getString("LastUpdate"));\
    // System.out.println(response);
    // } catch (IOException e) {
    // Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    // }
    // return true;
    // }

    // public static void writeToEneffcoRawDatapoint(String datapointId, String
    // json, String comment) {
    // try {
    // comment = comment.replace(" ", "_");
    // URL url;
    // // if (comment != null && comment != "")
    // url = new URL(ENEFFCO_BASE_URL + "/rawdatapoint/" + datapointId +
    // "/value?comment=\"" + comment + "\"");
    // // else
    // // url = new URL(ENEFFCO_BASE_URL + "/rawdatapoint/" + datapointId +
    // "/value");
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // connection.setRequestMethod("POST");
    // connection.setDoOutput(true);
    // connection.setDoInput(true);
    // connection.setRequestProperty("Accept", "application/json");
    // connection.setRequestProperty("Content-Type", "application/json");
    // connection.setRequestProperty("Authorization", tokenEneffco);

    // // connection.setRequestProperty("Content-Length",...);
    // OutputStream os = connection.getOutputStream();
    // os.write(json.getBytes("UTF-8"));
    // os.close();

    // // read the response
    // int statusCode = connection.getResponseCode();
    // // System.out.println(statusCode);
    // InputStream in = new BufferedInputStream(connection.getInputStream());
    // String result = IOUtils.toString(in, "UTF-8");
    // System.out.println(result);

    // in.close();
    // connection.disconnect();
    // } catch (Exception e) {
    // Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    // }
    // }
}
