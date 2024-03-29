package com.ewus.ba.facilityService.controller;

import com.ewus.ba.facilityService.Datenbankverbindung;
import com.ewus.ba.facilityService.EneffcoUtils;
import com.ewus.ba.facilityService.EnergielenkerUtils;
import com.ewus.ba.facilityService.Utils;
import com.ewus.ba.facilityService.model.AnalysisLog;
import com.ewus.ba.facilityService.model.EneffcoValue;
import com.ewus.ba.facilityService.model.Facility;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class FacilityController {
  private static Connection dbConnection = new Datenbankverbindung().getConnection();
  private Map<String, Facility> facilitiesMap = new HashMap<>();

  @GetMapping("/facilities-data-list")
  @ResponseBody
  public ArrayList<Facility> fillFacilities(@RequestParam String codes) {
    String[] codesArray;

    if (codes == null) {
      codesArray = getFacilityCodes().stream().toArray(String[]::new);
    } else {
      codesArray = codes.trim().replaceAll("[\\[|\\]|\"|\\s]", "").split(",");
    }

    ArrayList<Facility> facilities =
        Stream.of(codesArray)
            .map(code -> new Facility(code))
            .collect(Collectors.toCollection(ArrayList::new));

    EnergielenkerUtils.loginEnergielenker();
    facilities = EnergielenkerUtils.fillEnergielenkerObjectIds(dbConnection, facilities);

    try {
      for (int i = 0; i < facilities.size(); i++) {
        try {
          // Fill fields of Energielenker Object Einsparzählerprotokoll
          facilities.set(
              i,
              EnergielenkerUtils.fillEnergielenkerFields(
                  facilities.get(i).getEinsparzaehlerObjectId(), facilities.get(i)));
          // Fill fields of Energielenker Object Liegenschaft
          EnergielenkerUtils.fetchLiegenschaftFieldValues(dbConnection, facilities.get(i));
          facilities.get(i).calcTww();

          // Fill fields of Energielenkerobject Anlagentechnik
          facilities.set(
              i,
              EnergielenkerUtils.fillEnergielenkerFields(
                  facilities.get(i).getAnlagentechnikObjectId(), facilities.get(i)));

          // Fill fields of Energielenker-object Regelparameter_Soll-Werte
          facilities.set(
              i,
              EnergielenkerUtils.fillEnergielenkerFields(
                  facilities.get(i).getRegelparameterSollWerteObjectId(), facilities.get(i)));

          EneffcoUtils.fetchEneffcoIds(dbConnection, facilities.get(i));
        } catch (Exception e) {
          Utils.LOGGER.warn(
              "Error fillFacilities at: " + facilities.get(i).getCode() + e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }

    facilitiesMap.putAll(facilities.stream().collect(Collectors.toMap(f -> f.getCode(), f -> f)));
    return facilities;
  }

  @GetMapping("/facility-codes")
  @ResponseBody
  public List<String> getFacilityCodes() {
    return EnergielenkerUtils.fetchAllFacilityCodes(dbConnection);
  }

  @PostMapping("/text-fragments")
  public ResponseEntity postTextFragments(@RequestBody Map<String, String> body) {
    Facility facility = facilitiesMap.get(body.get("id").toUpperCase());

    if (facility == null) {
      facility = fillFacilities(body.get("id")).get(0);
    }

    try {
      // Save old
      String[] prev =
          EnergielenkerUtils.getStringWithCreationTimeEnergielenker(
              facility.getRegelparameterSollWerteObjectId(), facility.getTextFragmentsId());

      if (prev != null && !prev[0].isEmpty()) {
        EnergielenkerUtils.postStringEnergielenker(
            facility.getRegelparameterSollWerteObjectId(),
            facility.getTextFragmentsPrevId(),
            prev[1] + ": " + prev[0]);
      }
      // Save new
      String textFragmentsNew = body.get("textFragments");
      if (textFragmentsNew != null) {
        // Allow saving blank textfragmens
        if (textFragmentsNew == "") {
          textFragmentsNew = " ";
        }
        EnergielenkerUtils.postStringEnergielenker(
            facility.getRegelparameterSollWerteObjectId(),
            facility.getTextFragmentsId(),
            textFragmentsNew.replace("\n", "   "));
      } else {
        return ResponseEntity.badRequest()
            .body("The field 'textFragments' is required, but not present");
      }

      logPostTextFragments(
          facility.getCode(), body.get("textFragmentsAnalysisResult"), textFragmentsNew);
    } catch (Exception e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
    return ResponseEntity.created(null).build();
  }

  private void logPostTextFragments(
      String code, String textFragmentsAnalysisResult, String textFragments) {
    AnalysisLog analysisLog = new AnalysisLog(code, textFragmentsAnalysisResult, textFragments);
    try {
      Statement statement = dbConnection.createStatement();
      String selectSql =
          "INSERT INTO [facility_analysis_logs] "
              + "([anlagencode],[textbausteine_analyseergebnis],[textbausteine_gespeichert],[textbausteine_unterschied],[zeitstempel]) VALUES ('"
              + analysisLog.getCode()
              + "','"
              + analysisLog.getTextFragmentsAnalysisResult()
              + "','"
              + analysisLog.getTextFragments()
              + "','"
              + analysisLog.getTextFragmentsDiff()
              + "','"
              + analysisLog.getTimestamp()
              + "');";
      statement.executeUpdate(selectSql);
    } catch (SQLException e) {
      Utils.LOGGER.warn(e.getMessage(), e);
    }
  }

  @GetMapping("/eneffco/datapoint/{id}/values")
  @ResponseBody
  public List<EneffcoValue> getEneffcoValues(
      @PathVariable(value = "id") String datapointId,
      @RequestParam String from,
      @RequestParam String to,
      @RequestParam int timeInterval,
      @RequestParam boolean includeNanValues) {
    return EneffcoUtils.readEneffcoDatapointValues(
        datapointId, from, to, timeInterval, includeNanValues);
  }
}
