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
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

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
@RequestMapping(value = "/energielenker")
public class EnergielenkerController {
    private static Connection dbConnection = new Datenbankverbindung().getConnection();

    // TODO: pass codes and only fill those. Reusie filter logic from ACO
    @GetMapping("/fill-el-objects")
    @ResponseBody
    public static ArrayList<Facility> fillFacilities(@RequestBody String codesJson) {
        System.out.println(codesJson);
        // TODO: use library to parse json array
        String[] codesArray = codesJson.strip().replace("[", "").replace("]", "").split(",");
        System.out.println(Arrays.toString(codesArray));
        System.out.println("fillFacilities()");
        EnergielenkerUtils.loginEnergielenker();
		ArrayList<Facility> facilities = new ArrayList<Facility>();
		ArrayList<Facility> facilitiesFiltered = new ArrayList<Facility>();

		facilities = EnergielenkerUtils.getESZenergielenkerTableEinsparzaehler(dbConnection, facilities);

		try {
			for (int i = 0; i < facilities.size(); i++) {
				// objects
				facilities.get(i).setCode(EnergielenkerUtils.getAnlagencode(dbConnection,
						facilities.get(i).getEinsparzaehlerobjektid()));
                // Filter all facilities with codes passed in codesJson 
                String codeToCheck = facilities.get(i).getCode().toLowerCase();
                if(Arrays.stream(codesArray).anyMatch(s -> s != null && s.toLowerCase().contains(codeToCheck))) {
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
					Utils.LOGGER.log(Level.WARNING, "Error fillFacilities at: " + facilitiesFiltered.get(i).getCode() + "\n",
							"errors.log");
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
