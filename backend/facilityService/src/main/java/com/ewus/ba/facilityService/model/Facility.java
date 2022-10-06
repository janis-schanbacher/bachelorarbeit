package com.ewus.ba.facilityService.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Facility {

  private String code; // Code der Anlage, z.b. TST_001

  private int wmzEneffco; // Einsparzaehlerprotokoll> 190 WMZ Eneffco (letzte Stelle im
  // DP Code)

  private String vorlaufId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.VL.[wmzEneffco]
  // z.b. "4a3a0973-2fc4-47b6-aaf7-1ddd52bc94af"

  private String ruecklaufId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.RL.[wmzEneffco]

  private String volumenstromId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.VS.[wmzEneffco]

  private String leistungId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.L.[wmzEneffco]

  private String nutzungsgradId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.ETA.[wmzEneffco]

  private String auslastungKgrId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.AUS.MAX.[wmzEneffco]

  private String deltaTemperatureId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.DT.[wmzEneffco]

  private String aussentemperaturCode; // EL>Liegenschaft> 038 zugeordnete
  // Außentemperatur

  private boolean brennwertkessel; // EL>Anlagentechnik> 011 Brennwertkessel

  private String versorgungstyp; // EL>Liegenschaft> 030 Versorgungstyp

  private boolean tww;

  private double utilizationRatePreviousWeek; // ESZ > 103 Nutzungsgrad Vorwoche

  private String textFragmentsId; // Einsparzaehlerprotokoll > Regelparameter_Soll-Werte >
  // 960 AKTUELL
  // Textbausteine Auto Analyse

  private String textFragmentsPrevId; // Einsparzaehlerprotokoll >
  // Regelparameter_Soll-Werte > 961 ALT
  // Textbausteine Auto Analyse

  private String textFragments; // Content of Einsparzaehlerprotokoll > Regelparameter_Soll-Werte >
  // 960 AKTUELL
  // Textbausteine Auto Analyse

  private String einsparzaehlerObjectId;

  private String liegenschaftObjectId;

  private String regelparameterSollWerteObjectId;

  private String anlagentechnikObjectId;

  public Facility(String code) {
    this.code = code;
  }

  public Facility() {}

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getWmzEneffco() {
    return this.wmzEneffco;
  }

  public void setWmzEneffco(int wmzEneffco) {
    this.wmzEneffco = wmzEneffco;
  }

  public String getVorlaufId() {
    return vorlaufId;
  }

  public void setVorlaufId(String vorlaufId) {
    this.vorlaufId = vorlaufId;
  }

  public String getRuecklaufId() {
    return ruecklaufId;
  }

  public void setRuecklaufId(String ruecklaufId) {
    this.ruecklaufId = ruecklaufId;
  }

  public String getVolumenstromId() {
    return volumenstromId;
  }

  public void setVolumenstromId(String volumenstromId) {
    this.volumenstromId = volumenstromId;
  }

  public String getAussentemperaturCode() {
    return aussentemperaturCode;
  }

  public void setAussentemperaturCode(String aussentemperaturCode) {
    this.aussentemperaturCode = aussentemperaturCode;
  }

  public boolean getBrennwertkessel() {
    return brennwertkessel;
  }

  public void setBrennwertkessel(boolean brennwertkessel) {
    this.brennwertkessel = brennwertkessel;
  }

  public String getNutzungsgradId() {
    return nutzungsgradId;
  }

  public void setNutzungsgradId(String nutzungsgradId) {
    this.nutzungsgradId = nutzungsgradId;
  }

  public String getAuslastungKgrId() {
    return auslastungKgrId;
  }

  public void setAuslastungKgrId(String auslastungKgrId) {
    this.auslastungKgrId = auslastungKgrId;
  }

  public String getDeltaTemperatureId() {
    return deltaTemperatureId;
  }

  public void setDeltaTemperatureId(String deltaTemperatureId) {
    this.deltaTemperatureId = deltaTemperatureId;
  }

  public String getLeistungId() {
    return leistungId;
  }

  public void setLeistungId(String leistungId) {
    this.leistungId = leistungId;
  }

  public boolean getTww() {
    return tww;
  }

  public void setTww(boolean tww) {
    this.tww = tww;
  }

  public double getUtilizationRatePreviousWeek() {
    return utilizationRatePreviousWeek;
  }

  public void setUtilizationRatePreviousWeek(double utilizationRatePreviousWeek) {
    this.utilizationRatePreviousWeek = utilizationRatePreviousWeek;
  }

  public String getTextFragmentsId() {
    return textFragmentsId;
  }

  public void setTextFragmentsId(String textFragmentsId) {
    this.textFragmentsId = textFragmentsId;
  }

  public String getTextFragmentsPrevId() {
    return textFragmentsPrevId;
  }

  public void setTextFragmentsPrevId(String textFragmentsPrevId) {
    this.textFragmentsPrevId = textFragmentsPrevId;
  }

  public String getTextFragments() {
    return textFragments;
  }

  public void setTextFragments(String textFragments) {
    this.textFragments = textFragments;
  }

  public String getEinsparzaehlerObjectId() {
    return einsparzaehlerObjectId;
  }

  public void setEinsparzaehlerObjectId(String einsparzaehlerObjectId) {
    this.einsparzaehlerObjectId = einsparzaehlerObjectId;
  }

  public String getVersorgungstyp() {
    return versorgungstyp;
  }

  public void setVersorgungstyp(String versorgungstyp) {
    this.versorgungstyp = versorgungstyp;
  }

  public String getLiegenschaftObjectId() {
    return liegenschaftObjectId;
  }

  public void setLiegenschaftObjectId(String liegenschaftObjectId) {
    this.liegenschaftObjectId = liegenschaftObjectId;
  }

  public String getRegelparameterSollWerteObjectId() {
    return regelparameterSollWerteObjectId;
  }

  public void setRegelparameterSollWerteObjectId(String regelparameterSollWerteObjectId) {
    this.regelparameterSollWerteObjectId = regelparameterSollWerteObjectId;
  }

  public String getAnlagentechnikObjectId() {
    return anlagentechnikObjectId;
  }

  public void setAnlagentechnikObjectId(String anlagentechnikObjectId) {
    this.anlagentechnikObjectId = anlagentechnikObjectId;
  }

  public void calcTww() {
    if (wmzEneffco != 1) {
      this.tww = false;
    } else {
      this.tww = versorgungstyp.toLowerCase().contains("tww");
    }
  }

  @Override
  public String toString() {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "";
    }
  }
}
