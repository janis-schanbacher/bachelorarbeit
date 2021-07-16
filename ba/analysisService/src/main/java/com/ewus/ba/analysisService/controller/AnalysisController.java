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

/**
 * Controller to perform analysises based on Configuration and store the results
 */
@RestController
@RequestMapping(value = "/analysis")
public class AnalysisController {
    @PostMapping
    public static String analyse(@RequestBody String codesJson) {
        // TODO: use library to parse json array
        String[] codesArray = codesJson.strip().replace("[", "").replace("]", "").split(",");

        for (String code : codesArray) {
            // TODO: create analyse objects and fill it with EL + Eneffco Data (either here
            // or outside body for all. If for all can be done in one EL request its better,
            // because EL workload has prio)
            // TODO: retrieve configs for all codes and save in map
            // TODO: create Textblocks from Data

            List<String> textblocks = new LinkedList<>();
            // TODO: get analysises to be run for code from DB
            textblocks.add(analysis1(code));
            // TODO: Save results to database (Create DB table)
        }

        // TODO: Send results as JSON return value

        return "";
    }

    private static String analysis1(String analyseJSONObject) {
        // TODO: Maybe use AnalysisObject model instead of json
        // TODO: check conditions and return Textblock
        return "";
    }
}
