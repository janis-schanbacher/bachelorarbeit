package com.ewus.ba.energielenkerService;

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
import com.ewus.ba.energielenkerService.model.ElObject;

// TODO: telete if not necessary. Otherwise rename MS, f.i.. to dataService. wär aber schön wenns ausschließlich energielenker

public class EneffcoUtils {
    public static String tokenEneffco = "Basic " + Config.readProperty("src/main/resources/dbConfig.properties", "tokenEneffco");

    public static String ENEFFCO_BASE_URL = "https://ewus.eneffco.de/api/v1.0";

    // public static void setEneffcoToken() {
    //     tokenEneffco = "Basic " + Config.readProperty("config.properties", "tokenEneffco");
    //     // Alternatively generate using eneffco credentials
    //     // try {
    //     // tokenEneffco = "Basic " + Base64.getEncoder()
    //     // .encodeToString(("username:password").getBytes("UTF-8"));
    //     // System.out.println(tokenEneffco);
    //     // } catch (Exception e) {
    //     // System.out.println(e);
    //     // }
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
    public static void fetchEneffcoIds(Connection c, ElObject elObject) {
        System.out.println(
                "Fetching eneffco ids for: " + elObject.getCode() + ", wmzEneffco: " + elObject.getWmzEneffco());
        try {
            elObject.setVorlauf(getEneffcoId(c, elObject.getCode() + ".WEZ.WMZ.VL." + elObject.getWmzEneffco()));
            elObject.setRuecklauf(getEneffcoId(c, elObject.getCode() + ".WEZ.WMZ.RL." + elObject.getWmzEneffco()));
            elObject.setVolumenstrom(getEneffcoId(c, elObject.getCode() + ".WEZ.WMZ.VS." + elObject.getWmzEneffco()));
            elObject.setLeistung(getEneffcoId(c, elObject.getCode() + ".WEZ.WMZ.L." + elObject.getWmzEneffco()));
            // System.out.println("Getting aussentemperatur based on code: " +
            // elObject.getAussentemperaturCode() + "...\n");
            elObject.setAussentemperatur(getEneffcoId(c, elObject.getAussentemperaturCode()));
            System.out.println("## Eneffco ids: " + elObject.getCode());
            System.out.println("Vorlauf: " + elObject.getVorlauf());
            System.out.println("Ruecklauf: " + elObject.getRuecklauf());
            System.out.println("Volumenstrom: " + elObject.getVolumenstrom());
            System.out.println("Leistung: " + elObject.getLeistung());
            System.out.println("Aussentemperatur: " + elObject.getAussentemperatur());
        } catch (Exception e) {
            // System.err.println("# Error fetchEneffcoIds: " + elObject.getCode());
            Utils.LOGGER.log(Level.WARNING, "Error fetchEneffcoIds: " + elObject.getCode() + e.getMessage(), e);
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

    // public static String readEneffcoDatapoint(String datapointId) {
    // try {
    // URL url;
    // url = new URL(ENEFFCO_BASE_URL + "/datapoint/" + datapointId);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // connection.setRequestMethod("GET");
    // connection.setRequestProperty("Accept", "application/json");
    // connection.setRequestProperty("Content-Type", "application/json");
    // connection.setRequestProperty("Authorization", tokenEneffco);

    // // read the response
    // int statusCode = connection.getResponseCode();
    // System.out.println(statusCode);
    // InputStream in = new BufferedInputStream(connection.getInputStream());
    // String result = IOUtils.toString(in, "UTF-8");
    // System.out.println(result);

    // in.close();
    // connection.disconnect();
    // return result;
    // } catch (Exception e) {
    // Utils.LOGGER.log(Level.WARNING,e.getMessage(), e);
    // return "";
    // }
    // }

    public static boolean checkIfEneffcoRawDatapointIsEmpty(String datapointId) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder().url(ENEFFCO_BASE_URL + "/rawdatapoint/" + datapointId + "/live")
                .addHeader("accept", "application/json").addHeader("Authorization", tokenEneffco).build();
        System.out.println("checkIfEneffcoRawDatapointIsEmpty request: " + request);
        try {
            JSONObject response = new JSONObject(client.newCall(request).execute().body().string());
            // System.out.println("Last update: " + response.getString("LastUpdate"));\
            System.out.println(response);
        } catch (IOException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return true;
    }

    public static void writeToEneffcoRawDatapoint(String datapointId, String json, String comment) {
        try {
            comment = comment.replace(" ", "_");
            URL url;
            // if (comment != null && comment != "")
            url = new URL(ENEFFCO_BASE_URL + "/rawdatapoint/" + datapointId + "/value?comment=\"" + comment + "\"");
            // else
            // url = new URL(ENEFFCO_BASE_URL + "/rawdatapoint/" + datapointId + "/value");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", tokenEneffco);

            // connection.setRequestProperty("Content-Length",...);
            OutputStream os = connection.getOutputStream();
            os.write(json.getBytes("UTF-8"));
            os.close();

            // read the response
            int statusCode = connection.getResponseCode();
            // System.out.println(statusCode);
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String result = IOUtils.toString(in, "UTF-8");
            System.out.println(result);

            in.close();
            connection.disconnect();
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }
}
