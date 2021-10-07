package com.ewus.ba.facilityService.controller;

import com.ewus.ba.facilityService.Datenbankverbindung;
import com.ewus.ba.facilityService.EneffcoUtils;
import com.ewus.ba.facilityService.EnergielenkerUtils;
import com.ewus.ba.facilityService.Utils;
import com.ewus.ba.facilityService.model.EneffcoValue;
import com.ewus.ba.facilityService.model.Facility;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
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
    System.out.println("Fill Facilities: " + codes);
    String[] codesArray = codes.strip().replaceAll("[\\[|\\]|\"|\\s]", "").split(",");

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

          // Fill fields of Energielenker-object Regelparameter_Soll-Werte
          facilities.set(
              i,
              EnergielenkerUtils.fillEnergielenkerFields(
                  facilities.get(i).getRegelparameterSollWerteObjectId(), facilities.get(i)));

          EneffcoUtils.fetchEneffcoIds(dbConnection, facilities.get(i));
        } catch (Exception e) {
          Utils.LOGGER.log(
              Level.WARNING,
              "Error fillFacilities at: " + facilities.get(i).getCode() + "\n");
          Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }

    facilitiesMap.putAll(facilities.stream().collect(Collectors.toMap(f -> f.getCode(), f -> f)));
    System.out.println("Done filling Facilities");
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
    // TODO: , set 960 to body.textfragments, save
    // body to db/log
    try {
      // Save old
      String[] prev =
          EnergielenkerUtils.getStringEnergielenker(
              facility.getRegelparameterSollWerteObjectId(), facility.getTextFragmentsId());

      if (prev != null && !prev[0].isBlank()) {
        EnergielenkerUtils.postStringEnergielenker(
            facility.getRegelparameterSollWerteObjectId(),
            facility.getTextFragmentsPrevId(),
            prev[1] + ": " + prev[0]);
      }
      // Save new
      String textFragmentsNew = body.get("textFragments");
      if (textFragmentsNew != null) {
        EnergielenkerUtils.postStringEnergielenker(
            facility.getRegelparameterSollWerteObjectId(),
            facility.getTextFragmentsId(),
            textFragmentsNew.replace("\n", "   ")); // body.get("textFragments"));
      } else {
        return ResponseEntity.badRequest()
            .body("The field 'textFragments'is required, but not present");
      }
      // TODO: log: saved textFragments, diff to textFraagmentsAnalysisResult, timestamp,
      // code
      // EnergielenkerUtils.getStringEnergielenker(facility.getRegelparameterSollWerteObjectId(),
      // facility.getTextFragmentsId());

    } catch (Exception e) {
      // TODO Auto-generated catch block

      e.printStackTrace();
    } // TODO
    return ResponseEntity.created(null)
        .build(); // EnergielenkerUtils.fetchAllFacilityCodes(dbConnection);
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