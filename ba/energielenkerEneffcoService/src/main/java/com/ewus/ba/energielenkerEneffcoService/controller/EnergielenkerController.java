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
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

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

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RestController
// @RequestMapping(value = "/")
public class EnergielenkerController {
	private static Connection dbConnection = new Datenbankverbindung().getConnection();

	// TODO: pass codes and only fill those. Reusie filter logic from ACO
	@GetMapping("/fill-facilities")
	@ResponseBody
	public static ArrayList<Facility> fillFacilities(@RequestBody String codesJson) {
		// public static ArrayList<Facility> fillFacilities(@RequestBody String
		// codesJson) {
		System.out.println(codesJson);
		// TODO: use library to parse json array
		String[] codesArray = codesJson.strip().replace("[", "").replace("]", "").split(",");
		System.out.println(Arrays.toString(codesArray));
		System.out.println("fillFacilities()");
		EnergielenkerUtils.loginEnergielenker();
		ArrayList<Facility> facilities = new ArrayList<Facility>();
		ArrayList<Facility> facilitiesFiltered = new ArrayList<Facility>();

		final boolean MOCK_FILL_FACILITIES = true;

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
			facilityMock.setLiegenschaftObjektId("25355");
			facilitiesMock.add(facilityMock);

			// TODO: move rest of block to proper location

			// for (int i = 0; i < facilitiesMock.size(); i++) {
			// System.out.println(AnalysisController.analyseFacilitySize(facilitiesMock.get(i)));
			// System.out.println(AnalysisController.analyseDeltaTemperature(facilitiesMock.get(i)));
			// System.out.println("Done with " + facilitiesMock.get(i).getCode());
			// }

			System.out.println("Leaving fillFacilities()");
			return facilitiesMock;
		}

		facilities = EnergielenkerUtils.getESZenergielenkerTableEinsparzaehler(dbConnection, facilities);

		try {
			for (int i = 0; i < facilities.size(); i++) {
				// objects
				facilities.get(i).setCode(
						EnergielenkerUtils.getAnlagencode(dbConnection, facilities.get(i).getEinsparzaehlerobjektid()));
				// Filter all facilities with codes passed in codesJson
				String codeToCheck = facilities.get(i).getCode().toLowerCase();
				if (Arrays.stream(codesArray).anyMatch(s -> s != null && s.toLowerCase().contains(codeToCheck))) {
					facilitiesFiltered.add(facilities.get(i));
				}
			}

			for (int i = 0; i < facilitiesFiltered.size(); i++) {
				try {
					facilitiesFiltered = EnergielenkerUtils.GET_attributes_JSON_Einsparzaehler_KI(
							facilitiesFiltered.get(i).getEinsparzaehlerobjektid(), facilitiesFiltered, i);
					EnergielenkerUtils.fetchLiegenschaftFields(dbConnection, facilitiesFiltered.get(i));
					facilitiesFiltered.get(i).calcTww();
					EneffcoUtils.fetchEneffcoIds(dbConnection, facilitiesFiltered.get(i));
				} catch (Exception e) {
					System.err.println("Error fillFacilities: " + facilitiesFiltered.get(i).getCode());
					Utils.LOGGER.log(Level.WARNING,
							"Error fillFacilities at: " + facilitiesFiltered.get(i).getCode() + "\n", "errors.log");
					Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}

		// System.out.println(facilitiesFiltered.get(0).toString());
		return facilitiesFiltered;
		// return facilities;
	}
}
