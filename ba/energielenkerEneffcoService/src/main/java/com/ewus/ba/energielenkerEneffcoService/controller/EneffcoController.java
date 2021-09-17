package com.ewus.ba.energielenkerEneffcoService.controller;

import com.ewus.ba.energielenkerEneffcoService.EneffcoUtils;

import com.ewus.ba.energielenkerEneffcoService.model.EneffcoValue;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
  public List<EneffcoValue> getEneffcoValues(
      @PathVariable(value = "id") String datapointId,
      @RequestParam String from, @RequestParam String to,
      @RequestParam int timeInterval,
      @RequestParam boolean includeNanValues) {
    return  EneffcoUtils.readEneffcoDatapointValues(datapointId, from, to, timeInterval, includeNanValues);
  }
}
