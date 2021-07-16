package com.ewus.ba.energielenkerService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

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
import java.util.Properties;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ewus.ba.energielenkerService.Utils;
import com.ewus.ba.energielenkerService.Config;
import com.ewus.ba.energielenkerService.Datenbankverbindung;
import com.ewus.ba.energielenkerService.model.ElObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
// import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
@RequestMapping(value = "/energielenker")
public class EnergielenkerController {
    private static String tokenEnergielenker;

    private static Connection dbConnection = new Datenbankverbindung().getConnection();

    @GetMapping("/login")
    public static String loginEnergielenker() {
        try {
            Properties credentials = Config.readProperties("src/main/resources/dbConfig.properties",
                    new String[] { "energielenkerUsername", "energielenkerPassword" });
            String query_url = "https://ewus.elmonitor.de/api/login_check";
            String json = "{ \"username\" :  \"" + credentials.getProperty("energielenkerUsername")
                    + "\" , \"password\" : \"" + credentials.getProperty("energielenkerPassword") + "\" }";
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

    public static String getStringEnergielenker(String obId, String attributeId) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://ewus.elmonitor.de/api/v1/basemonitor/objects/" + obId + "/attributes/" + attributeId)
                .addHeader("accept", "application/json").addHeader("Authorization", "Bearer " + tokenEnergielenker)
                .build();
        try {
            JSONObject response = new JSONObject(client.newCall(request).execute().body().string());
            if (attributeId.equals("2882")) {
                String aussentempCode = response.getJSONArray("values").getJSONObject(0).getString("resolvedValue");
                return aussentempCode;
            } else if (attributeId.equals("2578")) {
                String versorgungstyp = response.getJSONArray("values").getJSONObject(0).getJSONObject("resolvedValue")
                        .getString("name");
                return versorgungstyp;
            }
        } catch (IOException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return "";
    }

    // TODO: update, so no ElObject is to be passed, just url/{code},
    // @PathVariable(value="code") String code
    @GetMapping("/fetch-liegenschaft-fields")
    public static void fetchLiegenschaftFields(@RequestBody String elObjectJson) {
        JSONObject elObjectJsonObject = new JSONObject(elObjectJson);
        ElObject elObject = new ElObject();
        try {
            elObject.setCode(elObjectJsonObject.getString("code"));
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Error reading JSON, invalid request...");
            return; // TODO
        }
        System.out.println(elObject.toString());
        ResultSet resultSet;
        try {
            Statement statement = dbConnection.createStatement();
            String selectSql;
            if (elObject.getEinsparzaehlerobjektid() == null || elObject.getEinsparzaehlerobjektid().equals("")) {
                // Create and execute a SELECT SQL statement.
                selectSql = "SELECT [Code], [ewus_assets].[dbo].[energielenker_objects].[parentId]"
                        + "FROM [ewus_assets].[dbo].[energielenker_sortiert]"
                        + "JOIN [ewus_assets].[dbo].[energielenker_objects]"
                        + "ON [ewus_assets].[dbo].[energielenker_sortiert].[ID] = [ewus_assets].[dbo].[energielenker_objects].[id]"
                        + "WHERE code = '" + elObject.getCode()
                        + "' AND [ewus_assets].[dbo].[energielenker_sortiert].[Name] = 'Anlagentechnik';";
            } else {
                selectSql = "SELECT parentid FROM [ewus_assets].[dbo].[energielenker_objects] WHERE id = '"
                        + elObject.getEinsparzaehlerobjektid() + "';";
            }
            System.out.println(selectSql);
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
                // System.out.println(resultSet.getString(1) + " | ");
                System.out.println(resultSet.getString(2));
                elObject.setLiegenschaftObjektId(resultSet.getString(2));

                // if (elObject.getCode().equalsIgnoreCase("ACO.001")) {
                // elObject.setLiegenschaftObjektId("25354");
                // } else if (elObject.getCode().equalsIgnoreCase("ACO.002")) {
                // elObject.setLiegenschaftObjektId("25355");
                // } else if (elObject.getCode().equalsIgnoreCase("ACO.003")) {
                // elObject.setLiegenschaftObjektId("25357");
                // } else if (elObject.getCode().equalsIgnoreCase("ACO.004")) {
                // elObject.setLiegenschaftObjektId("25356");
                // } else if (elObject.getCode().equalsIgnoreCase("ACO.005")) {
                // elObject.setLiegenschaftObjektId("25396");
                // } else if (elObject.getCode().equalsIgnoreCase("ACO.006")) {
                // elObject.setLiegenschaftObjektId("25397");
                // }

            }
            // TODO: use/remove
            // elObject.setAussentemperaturCode(getStringEnergielenker(elObject.getLiegenschaftObjektId(),
            // "2882"));
            // elObject.setVersorgungstyp(getStringEnergielenker(elObject.getLiegenschaftObjektId(),
            // "2578"));
            // System.out.println("elObject.getAussentemperaturCode(): " +
            // elObject.getAussentemperaturCode());
            // System.out.println("elObject.getVersorgungstyp(): " +
            // elObject.getVersorgungstyp());
            System.out.println("Done Fetching Liegenschaftsfields for: " + elObject.getCode());
            System.out.println(elObject.toString());
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        // TODO: return sth. e.g. json die fields, serialisiertes ELObject, ..
    }

    public static void postStringEnergielenker(String obId, String attributeId, String pValue) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create("{\n    \"value\": \"" + pValue + "\"\n}", mediaType);
        Request request = new Request.Builder()
                .url("https://ewus.elmonitor.de/api/v1/basemonitor/attributes/" + attributeId + "/values?entityId="
                        + obId + "&sourceId=1")
                .method("PATCH", body).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + tokenEnergielenker).build();
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

    public static void postIntEnergielenker(String obId, String attributeId, int pValue) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create("{\n    \"value\": " + pValue + "\n}", mediaType);
        Request request = new Request.Builder()
                .url("https://ewus.elmonitor.de/api/v1/basemonitor/attributes/" + attributeId + "/values?entityId="
                        + obId + "&sourceId=1")
                .method("PATCH", body).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + tokenEnergielenker).build();
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

    public static void postFloatEnergielenker(String obId, String attributeId, Float pValue) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create("{\n    \"value\": " + pValue + "\n}", mediaType);
        Request request = new Request.Builder()
                .url("https://ewus.elmonitor.de/api/v1/basemonitor/attributes/" + attributeId + "/values?entityId="
                        + obId + "&sourceId=1")
                .method("PATCH", body).addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + tokenEnergielenker).build();
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

    public static ArrayList<ElObject> GET_attributes_JSON_Einsparzaehler_KI(String pid, ArrayList<ElObject> elObjects,
            int index) {
        String query_url = "https://ewus.elmonitor.de/api/v1/basemonitor/objects/" + pid + "/attributes";

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

                // TODO: uncomment and change
                // if (jobject.get("name").toString().contains("040 Nachtabsenkung Start")) {
                // elObjects.get(index).setAbsenkungWeekStart(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("041 Nachtabsenkung Ende")) {
                // elObjects.get(index).setAbsenkungWeekEnd(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("042 Nachtabsenkung WE Start")) {
                // elObjects.get(index).setAbsenkungWeekendStart(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("043 Nachtabsenkung WE Ende")) {
                // elObjects.get(index).setAbsenkungWeekendEnd(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("310 Nachtabsenkung Wahrsch.
                // Mo-Do")) {
                // elObjects.get(index).setpAbsenkungMoDiMiDo(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("311 Nachtabsenkung Wahrsch.
                // Fr")) {
                // elObjects.get(index).setpAbsenkungFr(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("312 Nachtabsenkung Wahrsch.
                // Sa")) {
                // elObjects.get(index).setpAbsenkungSa(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("313 Nachtabsenkung Wahrsch.
                // So")) {
                // elObjects.get(index).setpAbsenkungSo(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("202 Nachtabsenkung letzte KI
                // Prüfung")) {
                // elObjects.get(index).setNachtabsenkungLetzteKiPruefungId(jobject.get("id").toString());
                // }

                // if (jobject.get("name").toString().contains("198 Anlagenüberwachung letzte KI
                // Prüfung")) {
                // elObjects.get(index).setAnlagenueberwachungLetzteKiPruefungId(jobject.get("id").toString());
                // }

                // if (jobject.get("name").toString().contains("211 Heizgrenze letzte KI
                // Prüfung")) {
                // elObjects.get(index).setHeizgrenzeLetzteKiPruefungId(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("050 Heizgrenze")) {
                // elObjects.get(index).setHeizgrenze(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("051 Heizgrenze Pumpe")) {
                // elObjects.get(index).setHeizgrenzePumpe(jobject.get("id").toString());
                // }

                // if (jobject.get("name").toString().contains("300 Heizkurve
                // y-Achsenabschnitt")) {
                // elObjects.get(index).setyAchsenabschnitt(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("301 Heizkurve Steigung")) {
                // elObjects.get(index).setSteigung(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("302 Heizkurve
                // MinimumAussentemp")) {
                // elObjects.get(index).setMinimumAussentemp(jobject.get("id").toString());
                // }
                // if (jobject.get("name").toString().contains("222 Heizkurve letzte KI
                // Prüfung")) {
                // elObjects.get(index).setHeizkurveLetzteKiPruefungId(jobject.get("id").toString());
                // }

                // // Values
                // if (jobject.get("name").toString().contains("190 WMZ Eneffco (letzte Stelle
                // im DP Code)")) {
                // elObjects.get(index)
                // .setWmzEneffco(Integer.parseInt(GET_attributeValueJson(pid,
                // jobject.get("id").toString())));
                // }

                // if (jobject.get("name").toString().contains("200 Nachtabsenkung
                // Abfrageintervall (Tage)")) {
                // elObjects.get(index).setNachtabsenkungAbfrageintervall(
                // Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                // }
                // if (jobject.get("name").toString().contains("201 Nachtabsenkung
                // Abfragezeitraum (Tage)")) {
                // elObjects.get(index).setNachtabsenkungAbfragezeitraum(
                // Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                // }
                // if (jobject.get("name").toString().contains("202 Nachtabsenkung letzte KI
                // Prüfung")) {
                // elObjects.get(index).setNachtabsenkungLetzteKiPruefung(
                // GET_attributeValueJson(pid, jobject.get("id").toString()));
                // System.out.println(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }
                // if (jobject.get("name").toString().contains("195 Anlagenüberwachung
                // Abfrageintervall (Tage)")) {
                // elObjects.get(index).setAnlagenueberwachungAbfrageintervall(
                // Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                // }
                // if (jobject.get("name").toString().contains("196 Anlagenüberwachung
                // Abfragezeitraum (Tage)")) {
                // elObjects.get(index).setAnlagenueberwachungAbfragezeitraum(
                // Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                // }
                // if (jobject.get("name").toString().contains("198 Anlagenüberwachung letzte KI
                // Prüfung")) {
                // elObjects.get(index).setAnlagenueberwachungLetzteKiPruefung(
                // GET_attributeValueJson(pid, jobject.get("id").toString()));
                // System.out.println(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }
                // if (jobject.get("name").toString().contains("210 Heizgrenze KI Prüfung")) {
                // GET_attributeValueJson(pid, jobject.get("id").toString());
                // elObjects.get(index).setHeizgrenzeKiPruefung(
                // (GET_attributeValueJson(pid, jobject.get("id").toString()).contains("true"))
                // ? true
                // : false);
                // }
                // if (jobject.get("name").toString().contains("211 Heizgrenze letzte KI
                // Prüfung")) {
                // elObjects.get(index)
                // .setHeizgrenzeLetzteKiPruefung(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // System.out.println(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }

                // if (jobject.get("name").toString().contains("220 Heizkurve Abfrageintervall
                // (Tage)")) {
                // elObjects.get(index).setHeizkurveAbfrageintervall(
                // Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                // }
                // if (jobject.get("name").toString().contains("221 Heizkurve Abfragezeitraum
                // (Tage)")) {
                // elObjects.get(index).setHeizkurveAbfragezeitraum(
                // Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                // }
                // if (jobject.get("name").toString().contains("222 Heizkurve letzte KI
                // Prüfung")) {
                // elObjects.get(index)
                // .setHeizkurveLetzteKiPruefung(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }
                // if (jobject.get("name").toString().contains("040 Nachtabsenkung Start")) {
                // elObjects.get(index).setNighttimeFrom(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }
                // if (jobject.get("name").toString().contains("041 Nachtabsenkung Ende")) {
                // elObjects.get(index).setNighttimeTo(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }
                // if (jobject.get("name").toString().contains("041 Nachtabsenkung Ende")) {
                // elObjects.get(index).setNighttimeTo(GET_attributeValueJson(pid,
                // jobject.get("id").toString()));
                // }
            }

            in.close();
            conn.disconnect();
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return elObjects;
    }

    // TODO: check that eszId and liegenschaftsID used correctly.
    // TODO: fill all selected obejcts, probably in analysis service, using EL
    // service and Eneffco Service
    @GetMapping("esz-table")
    public static ArrayList<ElObject> getESZenergielenkerTableEinsparzaehler() { // TODO: remove //ArrayList<ElObject>
                                                                                 // elObjects) {
        ArrayList<ElObject> elObjects = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            // SMBUS_48AC%/1/Volume%
            Statement statement = dbConnection.createStatement();
            {

                // Create and execute a SELECT SQL statement.
                String selectSql = "SELECT [id],[parentId] ,[categoryId] FROM [ewus_assets].[dbo].[energielenker_objects] Where deinstalled = 'false' and name like 'Einsparzählerprotokoll%' ";

                resultSet = statement.executeQuery(selectSql);

                // Print results from select statement
                while (resultSet.next()) {
                    // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
                    // System.out.println(resultSet.getString(1) + " | ");

                    ElObject build = new ElObject();

                    // build.setHeizgrenze("test");
                    build.setEinsparzaehlerobjektid(resultSet.getString(1));
                    // System.out.println("getESZenergielenkerTableEinsparzaehler: ");
                    // System.out.println(resultSet.getString(1));

                    elObjects.add(build);
                }

            }
        } catch (SQLException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return elObjects;
    }

    // TODO: für alle
    public static String getAnlagencode(String eszId) {

        String anlagenname = getstep2(getstep1(eszId));
        // System.out.println(getstep2(
        // getstep1(id)));
        if (anlagenname.contains(" (")) {
            String[] tkm = anlagenname.split("\\(");
            String anlagencode = tkm[1];
            anlagencode = anlagencode.replace(")", "");
            anlagencode = anlagencode.replace(" ", "");
            // System.out.print(anlagencode);
            return anlagencode;
        } else {
            try {
                anlagenname = getstep2(getstep1(getstep1(eszId)));

                String[] tkm = anlagenname.split("\\(");
                String anlagencode = tkm[1];
                anlagencode = anlagencode.replace(")", "");
                anlagencode = anlagencode.replace(" ", "");
                // System.out.print(anlagencode);
                return anlagencode;
            } catch (Exception e) {
                return "not found";
            }

        }

        // return "";
    }

    public static String getstep2(String pid) {

        ResultSet resultSet = null;
        try {
            // SMBUS_48AC%/1/Volume%
            Statement statement = dbConnection.createStatement();
            {

                // Create and execute a SELECT SQL statement.
                String selectSql = "SELECT [name],[parentId] ,[categoryId] FROM [ewus_assets].[dbo].[energielenker_objects] Where id = '"
                        + pid + "'";

                resultSet = statement.executeQuery(selectSql);

                // Print results from select statement
                while (resultSet.next()) {
                    // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
                    // System.out.println(resultSet.getString(1) + " | " + resultSet.getString(2) +
                    // " | ");
                    // eSZ_Einsparungen.add(resultSet.getString(1));
                    return resultSet.getString(1);

                }

            }
        } catch (SQLException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return "";
    }

    public static String getstep1(String pid) {

        ResultSet resultSet = null;
        try {
            // SMBUS_48AC%/1/Volume%
            Statement statement = dbConnection.createStatement();
            {

                // Create and execute a SELECT SQL statement.
                String selectSql = "SELECT [id],[parentId] ,[categoryId] FROM [ewus_assets].[dbo].[energielenker_objects] Where id = '"
                        + pid + "'";

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
        } catch (SQLException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }

        return "";
    }

    public static String GET_attributeValueJson(String poid, String paid) {
        // System.out.println(poid);
        // System.out.println(paid);
        String attributeValue = "";
        String query_url = "https://ewus.elmonitor.de/api/v1/basemonitor/objects/" + poid + "/attributes/" + paid;

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
            // System.out.println(result);
            // System.out.println("result after Reading JSON Response");

            // JSONObject myResponse = new JSONObject(result);

            ArrayList<String> listdata = new ArrayList<String>();
            // JSONArray jArray = new JSONArray(result);
            jobject = new JSONObject(result);
            // System.out.println(jobject.get("values"));
            JSONArray jArray = new JSONArray(jobject.get("values").toString());
            // System.out.println(jArray.get(0));
            JSONObject jobjectValue = null;
            if (jArray.length() > 0) {
                jobjectValue = new JSONObject(jArray.get(0).toString());
                // System.out.println(jobjectValue.get("resolvedValue").toString());
                attributeValue = jobjectValue.get("resolvedValue").toString();
            }
            // if (jArray != null) {
            // for (int i=0;i<jArray.length();i++){
            // listdata.add(jArray.getString(i));
            // }
            // }
            // System.out.println("token : "+myResponse.getString("token"));

            // for (int i = 0; i < jArray.length(); i++) {
            //
            // System.out.println(jArray.get(i));
            // String test01 = ""+jArray.get(i);
            // JSONObject jobject = new JSONObject(test01);
            //
            // System.out.println(jobject);
            // System.out.println(jobject.get("deinstalled"));
            // System.out.println(jobject.get("name"));
            //
            // rawInsert(dbVerbindung,jobject);
            // }
            in.close();
            conn.disconnect();
        } catch (Exception e) {
            System.err.println("# Error GET_attributeValueJson: ");
            System.err.println(query_url);
            System.err.println(jobject);
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return attributeValue;
    }

}
