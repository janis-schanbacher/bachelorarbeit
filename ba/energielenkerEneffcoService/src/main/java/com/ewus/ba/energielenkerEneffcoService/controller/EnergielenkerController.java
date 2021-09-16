package com.ewus.ba.energielenkerEneffcoService.controller;

import com.ewus.ba.energielenkerEneffcoService.Datenbankverbindung;
import com.ewus.ba.energielenkerEneffcoService.EneffcoUtils;
import com.ewus.ba.energielenkerEneffcoService.EnergielenkerUtils;
import com.ewus.ba.energielenkerEneffcoService.Utils;
import com.ewus.ba.energielenkerEneffcoService.model.Facility;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
// @RequestMapping(value = "/")
@CrossOrigin
public class EnergielenkerController {

  private static Connection dbConnection = new Datenbankverbindung().getConnection();

  private Map<String, Facility> facilitiesMap = new HashMap<>();

  @GetMapping("/fill-facilities")
  @ResponseBody
  public ArrayList<Facility> fillFacilities(@RequestParam String codesJson) {
    System.out.println(codesJson);
    String[] codesArray = codesJson.strip().replace("[", "").replace("]", "").split(",");

    System.out.println(Arrays.toString(codesArray));
    EnergielenkerUtils.loginEnergielenker();
    ArrayList<Facility> facilities = new ArrayList<Facility>();

    final boolean MOCK_FILL_FACILITIES = false;

    if (MOCK_FILL_FACILITIES) {
      ArrayList<Facility> facilitiesMock = new ArrayList<Facility>();
      Facility facilityMock = new Facility();
      facilityMock.setCode("ACO.002");
      facilityMock.setWmzEneffco(1);
      facilityMock.setVorlaufId("b1a35f44-abe1-4afc-82eb-372889775b29");
      facilityMock.setRuecklaufId("a20772af-076f-421e-9c64-a78b1ce5017b");
      facilityMock.setVolumenstromId("607f72ce-7172-40f3-a1de-c0305f3b482f");
      facilityMock.setLeistungId("ee669174-10db-4e40-9b67-6b88a15707ee");
      facilityMock.setAussentemperaturId("0e1cb31b-52b7-4732-848a-f87a29afab49");
      facilityMock.setNutzungsgradId("ab7407da-f066-40e6-a975-6288ffa5ddb3");
      facilityMock.setAuslastungKgrId("cd56cb68-f551-4eac-8db2-4ed8d1ac011f");
      facilityMock.setDeltaTemeratureId("3db141b3-5414-4dea-b696-51e89c74494e");
      facilityMock.setAussentemperaturCode("433_T");
      facilityMock.setVersorgungstyp("TWW + Heizung");
      facilityMock.setTww(true);
      facilityMock.setHeizgrenze("2918");
      facilityMock.setHeizgrenzePumpe("2919");
      facilityMock.setSteigung("2929");
      facilityMock.setMinimumAussentemp("2930");
      facilityMock.setEinsparzaehlerobjektid("27249");
      facilityMock.setLiegenschaftObjectId("25355");
      facilityMock.setRegelparameterSollWerteObjectId("30362");

      facilitiesMock.add(facilityMock);
      facilitiesMock =
          EnergielenkerUtils.fillEnergielenkerFields(
              facilitiesMock.get(0).getRegelparameterSollWerteObjectId(), facilitiesMock, 0);
      facilitiesMap.put(facilityMock.getCode().toUpperCase(), facilityMock);
      return facilitiesMock;
    }

    facilities = EnergielenkerUtils.getFacilities(dbConnection, codesArray);

    try {
      for (int i = 0; i < facilities.size(); i++) {
        try {
          // Fill fields of Energielenker-object Einsparzählerprotokoll
          facilities =
              EnergielenkerUtils.fillEnergielenkerFields(
                  facilities.get(i).getEinsparzaehlerobjektid(), facilities, i);
          EnergielenkerUtils.fetchLiegenschaftFieldValues(dbConnection, facilities.get(i));
          facilities.get(i).calcTww();

          // Fill fields of Energielenker-object Regelparameter_Soll-Werte
          facilities =
              EnergielenkerUtils.fillEnergielenkerFields(
                  facilities.get(i).getRegelparameterSollWerteObjectId(), facilities, i);

          EneffcoUtils.fetchEneffcoIds(dbConnection, facilities.get(i)); // TODO:
          // fetch
          // in
          // AnalysisController,
          // which
          // will
          // be
          // in
          // AnalysisService
        } catch (Exception e) {
          Utils.LOGGER.log(
              Level.WARNING,
              "Error fillFacilities at: " + facilities.get(i).getCode() + "\n",
              "errors.log");
          Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
        }
      }
    } catch (Exception e) {
      Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
    }

    facilitiesMap.putAll(facilities.stream().collect(Collectors.toMap(f -> f.getCode(), f -> f)));
    System.out.println(facilitiesMap);
    return facilities;
  }

  @GetMapping("/facility-codes")
  @ResponseBody
  public List<String> getFacilityCodes() {
    return EnergielenkerUtils.fetchAllFacilityCodes(dbConnection);
  }

  @PostMapping("/text-fragments")
  public ResponseEntity postTextFragments(@RequestBody Map<String, String> body) {
    System.out.println("postTextFragments(): " + body);
    Facility facility = facilitiesMap.get(body.get("code").toUpperCase());

    if (facility == null) { // TODO: check why facility never found yet in map.
      System.out.println("loading facility");
      facility = fillFacilities(body.get("code")).get(0);
      System.out.println(facility);
    }
    // TODO: , set 960 to body.textfragments, save
    // body to db/log
    try {
      // Save old
      String[] prev =
          EnergielenkerUtils.getStringEnergielenker(
              facility.getRegelparameterSollWerteObjectId(), facility.getTextFragments());
      // System.out
      // .println("facility.getRegelparameterSollWerteObjectId()" +
      // facility.getRegelparameterSollWerteObjectId());
      // System.out.println(" facility.getTextFragments(): " +
      // facility.getTextFragments());
      // System.out.println("prev: " + prev);
      if (prev != null && !prev[0].isBlank()) {
        EnergielenkerUtils.postStringEnergielenker(
            facility.getRegelparameterSollWerteObjectId(),
            facility.getTextFragmentsPrev(),
            prev[1] + ": " + prev[0]);
      }
      // Save new
      String textFragmentsNew = body.get("textFragments");
      if (textFragmentsNew != null) {
        EnergielenkerUtils.postStringEnergielenker(
            facility.getRegelparameterSollWerteObjectId(),
            facility.getTextFragments(),
            textFragmentsNew.replace("\n", "   ")); // body.get("textFragments"));
      } else {
        return ResponseEntity.badRequest()
            .body("The field 'textFragments'is required, but not present");
      }
      // TODO: log: analysisResult (if different), editedAnalysisResult, timestamp,
      // code
      // EnergielenkerUtils.getStringEnergielenker(facility.getRegelparameterSollWerteObjectId(),
      // facility.getTextFragments());

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } // TODO
    return ResponseEntity.created(null)
        .build(); // EnergielenkerUtils.fetchAllFacilityCodes(dbConnection);
  }
}
