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

import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/eneffco")
@CrossOrigin
public class EneffcoController {
  @GetMapping("datapoint/{id}/values")
  @ResponseBody
  public List<JSONObject> getEneffcoValues( @PathVariable(value = "id") String datapointId, @RequestParam String from, @RequestParam String to, @RequestParam int timeInterval, @RequestParam boolean includeNanValues) {
    List<JSONObject> values = EneffcoUtils.readEneffcoDatapointValues(datapointId, from, to, timeInterval,
        includeNanValues);
    System.out.println(values);
    return values;
  }
}
