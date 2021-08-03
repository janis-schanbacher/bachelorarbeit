package com.ewus.ba.analysisService.controller;

import java.util.LinkedList;
import java.util.List;

// import com.go/ogle.gson.JsonObject;

// import org.codehaus.jettison.json.JSONArray;
// import org.codehaus.jettison.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ewus.ba.analysisService.model.AnalysisObject;
import com.ewus.ba.analysisService.model.Configuration;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Controller to perform analysises based on Configuration and store the results
 */
@RestController
@RequestMapping(value = "/analysis")
public class AnalysisController {
    @PostMapping
    public static String analyse(@RequestBody String codesJson) {
        ObjectMapper objectMapper = new ObjectMapper(); 
        String filledFacilitiesJson; 

        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("http://energielenker-eneffco-service/energielenker/fill-el-objects")
                .addHeader("accept", "application/json")
                // .addHeader("Authorization", "Bearer " + tokenEnergielenker)
                .build();
        try {
            filledFacilitiesJson = client.newCall(request).execute().body().string();
        } catch (IOException e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        System.out.println(filledFacilitiesJson)
        


        // objectMapper.readValue()
        // TODO: use library to parse json array
        String[] codesArray = codesJson.strip().replace("[", "").replace("]", "").split(",");

        ArrayList<Configuration> analysisSettings = getAnalysisSettings(codesArray);
        
        // TODO: equivalent to fillKiObjects
		ArrayList<AnalysisObject> analysisObjects = new ArrayList<>(); 
        // TODO replace with requiest to http://localhost:8080/energielenker/esz-table and transform result to save in analysisObjects: okhttpClient

        // TODO: 
        // analysisObjects = EnergielenkerUtils.getESZenergielenkerTableEinsparzaehler(dbVerbindung.getConnection(), analysisObjects);


        
        for (String code : codesArray) {
            // TODO: retrieve configs for all codes and save in map
            
            // TODO: create analyse objects and fill it with EL + Eneffco Data (either here
            // or outside body for all. If for all can be done in one EL request its better,
            // because EL workload has prio)
            // TODO: create Textblocks from Data

            List<String> textblocks = new LinkedList<>();
            // TODO: get analysises to be run for code from DB
            textblocks.add(analysis1(code));
            // TODO: Save results to database (Create DB table)
        }

        // TODO: Send results as JSON return value

        return ""; 
    }

    
// TODO: check, implement
    private static ArrayList<Configuration> getAnalysisSettings(String[] codes){
        // TODO: retrieve Settings from config table and save to configurations usiing configuration.getCode())
                // TODO: instead of hard coded get settings from db.

        // get from db using select ... where code is in configurations.mapToCodesArray
        // Dann in configurations schreiben und diese zurückgeben
        ArrayList<Configuration> configurations = new ArrayList<>();
        for(String code : codes) { 

        }
        return configurations; // TODO: check if necessary or done because of call by reference
    }   

    private static String analysis1(String analyseJSONObject) {
        // TODO: Maybe use AnalysisObject model instead of json
        // TODO: check conditions and return Textblock
        return "";
    }
}
