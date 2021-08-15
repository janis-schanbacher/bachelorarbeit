package com.ewus.ba.energielenkerEneffcoService;

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
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.ewus.ba.energielenkerEneffcoService.model.Facility;

public class EnergielenkerUtils {
    public static String tokenEnergielenker;

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

    public static void fetchLiegenschaftFields(Connection connection, Facility facility) {
        ResultSet resultSet;
        try {

            Statement statement = connection.createStatement();
            String selectSql;
            if (facility.getEinsparzaehlerobjektid() == null || facility.getEinsparzaehlerobjektid().equals("")) {
                // Create and execute a SELECT SQL statement.
                selectSql = "SELECT [Code], [ewus_assets].[dbo].[energielenker_objects].[parentId]"
                        + "FROM [ewus_assets].[dbo].[energielenker_sortiert]"
                        + "JOIN [ewus_assets].[dbo].[energielenker_objects]"
                        + "ON [ewus_assets].[dbo].[energielenker_sortiert].[ID] = [ewus_assets].[dbo].[energielenker_objects].[id]"
                        + "WHERE code = '" + facility.getCode()
                        + "' AND [ewus_assets].[dbo].[energielenker_sortiert].[Name] = 'Anlagentechnik';";
            } else {
                selectSql = "SELECT parentid FROM [ewus_assets].[dbo].[energielenker_objects] WHERE id = '"
                        + facility.getEinsparzaehlerobjektid() + "';";
            }
            System.out.println(selectSql);
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
                // System.out.println(resultSet.getString(1) + " | ");

                System.out.println(resultSet.getString(1));
                facility.setLiegenschaftObjektId(resultSet.getString(1));

                // if (facility.getCode().equalsIgnoreCase("ACO.001")) {
                // facility.setLiegenschaftObjektId("25354");
                // } else if (facility.getCode().equalsIgnoreCase("ACO.002")) {
                // facility.setLiegenschaftObjektId("25355");
                // } else if (facility.getCode().equalsIgnoreCase("ACO.003")) {
                // facility.setLiegenschaftObjektId("25357");
                // } else if (facility.getCode().equalsIgnoreCase("ACO.004")) {
                // facility.setLiegenschaftObjektId("25356");
                // } else if (facility.getCode().equalsIgnoreCase("ACO.005")) {
                // facility.setLiegenschaftObjektId("25396");
                // } else if (facility.getCode().equalsIgnoreCase("ACO.006")) {
                // facility.setLiegenschaftObjektId("25397");
                // }

            }
            // TODO: check if to delte
            facility.setAussentemperaturCode(
                    EnergielenkerUtils.getStringEnergielenker(facility.getLiegenschaftObjektId(), "2882"));
            facility.setVersorgungstyp(
                    EnergielenkerUtils.getStringEnergielenker(facility.getLiegenschaftObjektId(), "2578"));
            System.out.println("facility.getAussentemperaturCode(): " + facility.getAussentemperaturCode());
            System.out.println("facility.getVersorgungstyp(): " + facility.getVersorgungstyp());
            System.out.println("Done Fetching Liegenschaftsfields for: " + facility.getCode());
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
    }

    public static void postStringEnergielenker(String obId, String attributeId, String pValue) throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create("{\n    \"value\": \"" + pValue + "\"\n}", mediaType);
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
        RequestBody body = RequestBody.create("{\n    \"value\": " + pValue + "\n}", mediaType);
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
        RequestBody body = RequestBody.create("{\n    \"value\": " + pValue + "\n}", mediaType);
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

    public static ArrayList<Facility> fillEnergielenkerFields(String pid, ArrayList<Facility> facilities, int index) {
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

                if (jobject.get("name").toString().contains("103 Nutzungsgrad Vorwoche")) {
                    facilities.get(index)
                            .setUtilizationRatePreviousWeek(Double.parseDouble(jobject.get("id").toString()));
                }
                if (jobject.get("name").toString().contains("040 Nachtabsenkung Start")) {
                    facilities.get(index).setAbsenkungWeekStart(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("041 Nachtabsenkung Ende")) {
                    facilities.get(index).setAbsenkungWeekEnd(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("042 Nachtabsenkung WE Start")) {
                    facilities.get(index).setAbsenkungWeekendStart(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("043 Nachtabsenkung WE Ende")) {
                    facilities.get(index).setAbsenkungWeekendEnd(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("310 Nachtabsenkung Wahrsch. Mo-Do")) {
                    facilities.get(index).setpAbsenkungMoDiMiDo(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("311 Nachtabsenkung Wahrsch. Fr")) {
                    facilities.get(index).setpAbsenkungFr(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("312 Nachtabsenkung Wahrsch. Sa")) {
                    facilities.get(index).setpAbsenkungSa(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("313 Nachtabsenkung Wahrsch. So")) {
                    facilities.get(index).setpAbsenkungSo(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("050 Heizgrenze")) {
                    facilities.get(index).setHeizgrenze(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("051 Heizgrenze Pumpe")) {
                    facilities.get(index).setHeizgrenzePumpe(jobject.get("id").toString());
                }

                if (jobject.get("name").toString().contains("300 Heizkurve y-Achsenabschnitt")) {
                    facilities.get(index).setyAchsenabschnitt(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("301 Heizkurve Steigung")) {
                    facilities.get(index).setSteigung(jobject.get("id").toString());
                }
                if (jobject.get("name").toString().contains("302 Heizkurve MinimumAussentemp")) {
                    facilities.get(index).setMinimumAussentemp(jobject.get("id").toString());
                }

                // Values
                if (jobject.get("name").toString().contains("190 WMZ Eneffco (letzte Stelle im DP Code)")) {
                    facilities.get(index)
                            .setWmzEneffco(Integer.parseInt(GET_attributeValueJson(pid, jobject.get("id").toString())));
                }
                if (jobject.get("name").toString().contains("040 Nachtabsenkung Start")) {
                    facilities.get(index).setNighttimeFrom(GET_attributeValueJson(pid, jobject.get("id").toString()));
                }
                if (jobject.get("name").toString().contains("041 Nachtabsenkung Ende")) {
                    facilities.get(index).setNighttimeTo(GET_attributeValueJson(pid, jobject.get("id").toString()));
                }
                if (jobject.get("name").toString().contains("041 Nachtabsenkung Ende")) {
                    facilities.get(index).setNighttimeTo(GET_attributeValueJson(pid, jobject.get("id").toString()));
                }
            }

            in.close();
            conn.disconnect();
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return facilities;
    }

    public static ArrayList<Facility> fillEnergielenkerEszIds(Connection dbVerbindung, ArrayList<Facility> facilities) {

        ResultSet resultSet = null;
        try {
            // SMBUS_48AC%/1/Volume%
            Statement statement = dbVerbindung.createStatement();
            {

                // Create and execute a SELECT SQL statement.
                String selectSql = "SELECT [id],[parentId] ,[categoryId] FROM [ewus_assets].[dbo].[energielenker_objects] Where deinstalled = 'false' and name like 'Einsparzählerprotokoll%' ";

                resultSet = statement.executeQuery(selectSql);

                // Print results from select statement
                while (resultSet.next()) {
                    // System.out.println(resultSet.getString(2) + " " + resultSet.getString(3));
                    // System.out.println(resultSet.getString(1) + " | ");

                    Facility build = new Facility();

                    // build.setHeizgrenze("test");
                    build.setEinsparzaehlerobjektid(resultSet.getString(1));
                    // System.out.println("fillEnergielenkerEszIds: ");
                    // System.out.println(resultSet.getString(1));

                    facilities.add(build);
                }

            }
        } catch (SQLException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        return facilities;
    }

    public static String getAnlagencode(Connection dbVerbindung, String id) {

        String anlagenname = getstep2(dbVerbindung, getstep1(dbVerbindung, id));
        // System.out.println(getstep2(dbVerbindung,
        // getstep1(dbVerbindung, id)));
        if (anlagenname.contains(" (")) {
            String[] tkm = anlagenname.split("\\(");
            String anlagencode = tkm[1];
            anlagencode = anlagencode.replace(")", "");
            anlagencode = anlagencode.replace(" ", "");
            // System.out.print(anlagencode);
            return anlagencode;
        } else {
            try {
                anlagenname = getstep2(dbVerbindung, getstep1(dbVerbindung, getstep1(dbVerbindung, id)));

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

    public static String getstep2(Connection dbVerbindung, String pid) {

        ResultSet resultSet = null;
        try {
            // SMBUS_48AC%/1/Volume%
            Statement statement = dbVerbindung.createStatement();
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

    public static String getstep1(Connection dbVerbindung, String pid) {

        ResultSet resultSet = null;
        try {
            // SMBUS_48AC%/1/Volume%
            Statement statement = dbVerbindung.createStatement();
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