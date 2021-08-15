package com.ewus.ba.energielenkerEneffcoService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.HttpUrl;
import okhttp3.Call;
import okhttp3.Callback;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ewus.ba.energielenkerEneffcoService.Utils;
import com.ewus.ba.energielenkerEneffcoService.Config;
import com.ewus.ba.energielenkerEneffcoService.Datenbankverbindung;
import com.ewus.ba.energielenkerEneffcoService.model.Facility;
import com.ewus.ba.energielenkerEneffcoService.EneffcoUtils;
import com.ewus.ba.energielenkerEneffcoService.EnergielenkerUtils;
import com.ewus.ba.energielenkerEneffcoService.controller.EnergielenkerController;

@RestController
// @RequestMapping(value = "/")
public class AnalysisController {
    final static int TIMEINTERVAL_15M = 900;
    final static int TIMEINTERVAL_DAY = 86400;
    final static int TIMEINTERVAL_WEEK = 86407;
    private static final OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS).build();

    @GetMapping("/analyse")
    @ResponseBody
    public static List<String> analyse(@RequestBody String codesJson) {
        System.out.println("entered analyse");
        List<Facility> facilities = EnergielenkerController.fillFacilities(codesJson);
        System.out.println("filled facilities. Size: " + facilities.size());
        List<String> textFragments = new ArrayList<>();
        for (Facility facility : facilities) {
            System.out.println(facility.getCode());
            // TODO: fetch configurations and only do desired analysis
            textFragments.add(analyseFacilitySize(facility));
            textFragments.add(analyseDeltaTemperature(facility));
            textFragments.add(analyseUtilizationRate(facility));
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
    public static String analyseFacilitySize(Facility facility) {
        List<JSONObject> values = EneffcoUtils.readEneffcoDatapointValues(facility.getAuslastungKgrId(),
                java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).minus(400, ChronoUnit.DAYS)
                        .toString(), // todo: change to more days
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
    public static String analyseUtilizationRate(Facility facility) {
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
    public static String analyseDeltaTemperature(Facility facility) {
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

    // TODO move
    public static float getAverageValue(List<JSONObject> values) {
        float sum = 0;
        for (int i = 0; i < values.size(); i++) {
            sum += values.get(i).getFloat("Value");
        }
        return sum / values.size();
    }

}
