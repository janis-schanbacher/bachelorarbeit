package com.ewus.ba.energielenkerEneffcoService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.concurrent.TimeUnit;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.HttpUrl;
import okhttp3.Call;
import okhttp3.Callback;

import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ewus.ba.energielenkerEneffcoService.Utils;
import com.ewus.ba.energielenkerEneffcoService.Config;
import com.ewus.ba.energielenkerEneffcoService.Datenbankverbindung;
import com.ewus.ba.energielenkerEneffcoService.model.Facility;
import com.ewus.ba.energielenkerEneffcoService.model.FacilityAnalysisConfiguration;
import com.ewus.ba.energielenkerEneffcoService.repository.IFacilityAnalysisConfigurationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ewus.ba.energielenkerEneffcoService.EneffcoUtils;
import com.ewus.ba.energielenkerEneffcoService.EnergielenkerUtils;
import com.ewus.ba.energielenkerEneffcoService.controller.EnergielenkerController;
import com.ewus.ba.energielenkerEneffcoService.controller.FacilityAnalysisConfigurationController;

@RestController
// @RequestMapping(value = "/")
public class AnalysisController {
    final static int TIMEINTERVAL_15M = 900;
    final static int TIMEINTERVAL_DAY = 86400;
    final static int TIMEINTERVAL_WEEK = 86407;
    private static final OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();
    final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/analyse")
    @ResponseBody
    public List<String> analyse(@RequestBody String codesJson) {
        System.out.println("entered analyse");

        // List<Facility> facilities = fillFacilities(codesJson); as request
        // TODO: use eureka url
        HttpUrl.Builder httpBuilder = HttpUrl.parse("http://localhost:8080/fill-facilities").newBuilder();
        httpBuilder.addQueryParameter("codesJson", codesJson);
        Request request = new Request.Builder().url(httpBuilder.build()).build();
        Response response = null;
        List<Facility> facilities = new ArrayList<>();
        try {
            response = client.newCall(request).execute();

            if (response.code() == 400 || response.code() == 500) {
                // TODO: proper logging
                System.out.println("Response fill-facilities: " + response.code());
                System.out.println(response);
                // TODO:""
                return null;
            }

            facilities = objectMapper.readValue(response.body().string(), new TypeReference<List<Facility>>() {
            });

        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        System.out.println("filled facilities. Size: " + facilities.size());

        // get facility configs using codesJson
        // use in for loop
        // TODO: save config in facility objects
        // TODO: use eureka url
        httpBuilder = HttpUrl.parse("http://localhost:8080/configs/get-list").newBuilder();
        httpBuilder.addQueryParameter("codesJson", codesJson);
        request = new Request.Builder().url(httpBuilder.build()).build();
        response = null;
        List<FacilityAnalysisConfiguration> configs = new ArrayList<>();
        try {
            response = client.newCall(request).execute();

            if (response.code() == 400 || response.code() == 500) {
                // TODO: proper logging
                System.out.println("Response /codes/get-list: " + response.code());
                System.out.println(response);
                return null;
            }
            configs = objectMapper.readValue(response.body().string(),
                    new TypeReference<List<FacilityAnalysisConfiguration>>() {
                    });
        } catch (Exception e) {
            Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
        System.out.println("Retrieved configs. Size: " + configs.size());

        List<String> textFragments = new ArrayList<>();
        for (Facility facility : facilities) {
            System.out.println(facility.getCode());

            // If no config availabe default to run all analyses is used
            // FacilityAnalysisConfiguration config = configs.stream().filter(c ->
            // facility.getCode().equals(c.getId()))
            // .findFirst().orElse(new FacilityAnalysisConfiguration(facility.getCode(),
            // true, true, true, true));
            FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(facility.getCode(), true, true,
                    true, true);
            System.out.println(config);
            // TODO: fetch configurations and only do desired analysis
            // ResponseEntity<FacilityAnalysisConfiguration> configResponse =
            // FacilityAnalysisConfigurationController
            // .getFacilityAnalysisConfiguration(facility.getCode());
            // FacilityAnalysisConfiguration config = configResponse.getBody();
            // if (configResponse.getStatusCode().value() == 204) { // TODO: falls nochmal
            // umgeschrieben: check if config
            // // == null
            // System.out.println(
            // "No config for " + facility.getCode() + " available. Using default to run all
            // analyses.");
            // config = new FacilityAnalysisConfiguration(facility.getCode(), true, true,
            // true, true);
            // }

            if (config.getFacilitySize()) {
                textFragments.add(analyseFacilitySize(facility));
            }
            if (config.getUtilizationRate()) {
                textFragments.add(analyseUtilizationRate(facility));
            }
            if (config.getDeltaTemperature()) {
                textFragments.add(analyseDeltaTemperature(facility));
            }
            if (config.getReturnTemperature()) {
                textFragments.add(analyseReturnTemperature(facility));
            }
            // TODO: add code to textFragements.
            // TODO: maybe following schema: response: ["ACO.001" : ["text..". "text2.."],
            // "ACO.002" : [...], oder als JSONObject mit Array für jeden Code
            System.out.println("Done Analysing " + facility.getCode());
        }
        textFragments.removeAll(Arrays.asList("", null));
        System.out.println(textFragments);
        return textFragments;
    }

    // TODO: move to analysis service
    // TODO: Bestimmung From, To
    public String analyseFacilitySize(Facility facility) {
        List<JSONObject> values = EneffcoUtils.readEneffcoDatapointValues(facility.getAuslastungKgrId(),
                java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).minus(365, ChronoUnit.DAYS)
                        .toString(), // TODO: change amount of days
                java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString(), TIMEINTERVAL_15M,
                false);

        float avgAuslastungKgr = getAverageValue(values);
        // TODO: fetch grenzwert and TextFragement from db
        String textFragment = avgAuslastungKgr > 80 ? ""
                : "Heizkessel ist überdimensioniert, Durchschnittliche Auslastung " + avgAuslastungKgr
                        + "%. Mögliche Maßnahmen: Brenner einstellen; andere Düse verbauen (geringere Heizleistung) oder Neubau";
        System.out.println("Avg. Nutzungsgrad zw. -10 und -14 Grad: " + getAverageValue(values));
        System.out.println(textFragment);
        return textFragment;
    }

    // TODO: move to analysis service
    public String analyseUtilizationRate(Facility facility) {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        String from, to;
        if (currentMonth <= 3) { // First months of year
            from = LocalDateTime.of(currentYear - 1, Month.NOVEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
            to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
        } else if (currentMonth >= 11 && currentDay > 14) { // at least 2 weeks in timesspan
            from = LocalDateTime.of(currentYear, Month.NOVEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
            to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();

        } else {
            from = LocalDateTime.of(currentYear - 1, Month.NOVEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
            to = LocalDateTime.of(currentYear, Month.MARCH, 28, 23, 59, 59).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
        }

        List<JSONObject> values = EneffcoUtils.readEneffcoDatapointValues(facility.getNutzungsgradId(), from, to,
                TIMEINTERVAL_DAY, false);

        float avgUtilizationRate = getAverageValue(values);

        String textFragment = "";
        // TODO: choose which condition first
        // werden soll in textbaustein. E.g. Avg Nutzungsgrad vorwoche
        final double LIMIT_UTILIZATION_RATE = facility.getBrennwertkessel() ? 90 : 80;
        if (avgUtilizationRate < LIMIT_UTILIZATION_RATE) {
            textFragment = "Die Anlage weist einen geringen Nutzungsgrad auf (Avg. Nutzungsgrad: " + avgUtilizationRate
                    + "%). Maßnahmen: Prüfen ob WMZ Gesamt gemessen wird; Anlagenanalyse durchführen.​";
        } else if (facility.getUtilizationRatePreviousWeek() != 0
                && facility.getUtilizationRatePreviousWeek() < LIMIT_UTILIZATION_RATE) {
            textFragment = "Die Anlage weist einen geringen Nutzungsgrad auf (Nutzungsgrad Vorwoche: "
                    + facility.getUtilizationRatePreviousWeek()
                    + "). Maßnahmen: Prüfen ob WMZ Gesamt gemessen wird; Anlagenanalyse durchführen.";
        }

        // TODO: fetch grenzwert and TextFragement from db

        System.out.println("avgUtilizationRate Eneffco: " + avgUtilizationRate + ", Utitilization Rate prev week EL:"
                + facility.getUtilizationRatePreviousWeek());
        System.out.println(textFragment);
        return textFragment;
    }

    // TODO: move to analysis service
    // TODO: Bestimmung From, To
    public String analyseDeltaTemperature(Facility facility) {
        int currentYear = LocalDate.now().getYear();
        String from = LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                .toInstant().toString();
        String to = LocalDateTime.of(currentYear, Month.FEBRUARY, 28, 23, 59, 59).atZone(ZoneId.of("Europe/Berlin"))
                .toInstant().toString();

        List<JSONObject> values = EneffcoUtils.readEneffcoDatapointValues(facility.getDeltaTemeratureId(), from, to,
                TIMEINTERVAL_15M, false);

        float avgDeltaTemperature = getAverageValue(values);
        System.out.println("Avg. Temperaturdifferenz: " + getAverageValue(values));
        String textFragment = "";
        // TODO: fetch grenzwerte and TextFragements from db
        if (!facility.getTww()) { // TODO: Check. hat brennwertkessel
            if (avgDeltaTemperature < 15) {
                // TODO Die Temperaturspreizung ist mit/um xK zu gering. Fragen ob entsprechend
                // ändern
                textFragment = "Die Temperaturspreizung ist mit " + avgDeltaTemperature
                        + "K zu gering. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
            }
        } else {
            if (avgDeltaTemperature < 10) {
                // TODO Die Temperaturspreizung ist mit/um xK zu gering. Fragen ob entsprechend
                // ändern
                textFragment = "Die Temperaturspreizung ist zu gering. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
            }
        }

        System.out.println(textFragment);
        return textFragment;
    }

    // TODO: move to analysis service
    public String analyseReturnTemperature(Facility facility) {
        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();
        int currentDay = LocalDate.now().getDayOfMonth();
        String from, to;
        if (currentMonth <= 2) { // First months of year
            from = LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
            to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
        } else if (currentMonth >= 12 && currentDay > 14) { // at least 2 weeks in timesspan
            from = LocalDateTime.of(currentYear, Month.DECEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
            to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();

        } else {
            from = LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
            to = LocalDateTime.of(currentYear, Month.FEBRUARY, 28, 23, 59, 59).atZone(ZoneId.of("Europe/Berlin"))
                    .toInstant().toString();
        }

        List<JSONObject> values = EneffcoUtils.readEneffcoDatapointValues(facility.getRuecklaufId(), from, to,
                TIMEINTERVAL_DAY, false);

        // TODO: fetch grenzwert and TextFragement from db
        final double LIMIT_PORTION_ACCEPTED_MIN = 95.0 / 100;
        final double LIMIT_RETURN_TEMPERATURE = 55;
        double portionOfValuesMarginBelowLimit = getPortionOfValuesMargin(values, LIMIT_RETURN_TEMPERATURE);

        String textFragment = "";
        // TODO: check 90% vs 95%/. Absichern, dass gut: alle werte von .RL.WMZ
        // betrachten, zählen wenn unter 55, prozentsatz bilden.Wenn Kein
        // Brennwertkessel kein Textbaustein
        if (portionOfValuesMarginBelowLimit < LIMIT_PORTION_ACCEPTED_MIN) {
            textFragment = "Brennwerteffekt wird nicht ausreichend genutzt. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge";
        }

        System.out.println("portionOfValuesMarginBelowLimit Eneffco: " + portionOfValuesMarginBelowLimit);
        System.out.println(textFragment);
        return textFragment;
    }

    // TODO move
    public float getAverageValue(List<JSONObject> values) {
        float sum = 0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i).getFloat("Value");
        }
        return sum / values.size();
    }

    public double getPortionOfValuesMargin(List<JSONObject> values, double limit) {
        int acceptedValuesCount = 0;
        System.out.println("limit: " + limit);
        for (int i = 0; i < values.size(); i++) {
            acceptedValuesCount += (values.get(i).getFloat("Value") < limit) ? 1 : 0;
        }
        return (double) acceptedValuesCount / values.size();
    }

}
