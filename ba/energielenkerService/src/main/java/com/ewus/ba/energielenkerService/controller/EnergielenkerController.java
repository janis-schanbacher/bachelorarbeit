package com.ewus.ba.energielenkerService.controller;

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

import com.ewus.ba.energielenkerService.Utils;
import com.ewus.ba.energielenkerService.Config;
import com.ewus.ba.energielenkerService.Datenbankverbindung;
import com.ewus.ba.energielenkerService.model.ElObject;
import com.ewus.ba.energielenkerService.EneffcoUtils;
import com.ewus.ba.energielenkerService.EnergielenkerUtils;


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
    public static ArrayList<ElObject> fillElObjects(@RequestBody String codesJson) {
        System.out.println(codesJson);
        // TODO: use library to parse json array
        String[] codesArray = codesJson.strip().replace("[", "").replace("]", "").split(",");
        System.out.println(Arrays.toString(codesArray));
        System.out.println("fillElObjects()");
        EnergielenkerUtils.loginEnergielenker();
		ArrayList<ElObject> elObjects = new ArrayList<ElObject>();
		ArrayList<ElObject> elObjectsFiltered = new ArrayList<ElObject>();

		elObjects = EnergielenkerUtils.getESZenergielenkerTableEinsparzaehler(dbConnection, elObjects);

		try {
			for (int i = 0; i < elObjects.size(); i++) {
				// objects
				elObjects.get(i).setCode(EnergielenkerUtils.getAnlagencode(dbConnection,
						elObjects.get(i).getEinsparzaehlerobjektid()));
                // Filter all elObjects with codes passed in codesJson 
                String codeToCheck = elObjects.get(i).getCode().toLowerCase();
                if(Arrays.stream(codesArray).anyMatch(s -> s != null && s.toLowerCase().contains(codeToCheck))) {
					elObjectsFiltered.add(elObjects.get(i));
				}
			}

			for (int i = 0; i < elObjectsFiltered.size(); i++) {
				try {
					elObjectsFiltered = EnergielenkerUtils.GET_attributes_JSON_Einsparzaehler_KI(
							elObjectsFiltered.get(i).getEinsparzaehlerobjektid(), elObjectsFiltered, i);
					EnergielenkerUtils.fetchLiegenschaftFields(dbConnection, elObjectsFiltered.get(i));
					elObjectsFiltered.get(i).calcTww();
					EneffcoUtils.fetchEneffcoIds(dbConnection, elObjectsFiltered.get(i));
				} catch (Exception e) {
					System.err.println("Error fillElObjects: " + elObjectsFiltered.get(i).getCode());
					Utils.LOGGER.log(Level.WARNING, "Error fillElObjects at: " + elObjectsFiltered.get(i).getCode() + "\n",
							"errors.log");
					Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			Utils.LOGGER.log(Level.WARNING, e.getMessage(), e);
		}
        // System.out.println(elObjectsFiltered.get(0).toString());
		return elObjectsFiltered;
		// return elObjects;
	}
}
