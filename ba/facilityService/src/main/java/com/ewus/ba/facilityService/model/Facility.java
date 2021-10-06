//     // TODO:
//     // - Add Variables for Analysis-Values
//     // - Add Variables for EL-ids (Felder der Textbausteine)

package com.ewus.ba.facilityService.model;

public class Facility {

  private String code; // Code der Anlage, z.b. STO_001

  private int wmzEneffco; // Einsparzaehlerprotokoll> 190 WMZ Eneffco (letzte Stelle im
  // DP Code)

  // vorlaufId, aussentemperatur, volumenstromId and leistungId are eneffco object
  // ids
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
  // [Anlagencode].WEZ.AUS.KGR.[wmzEneffco]

  private String deltaTemperatureId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.DT.[wmzEneffco]

  private String aussentemperaturCode; // EL>Liegenschaft> 038 zugeordnete
  // Außentemperatur

  private boolean brennwertkessel; // EL>Anlagentechnik> 011 Brennwertkessel

  // TODO 021 Brennwertkessel berücksichtigen, wahrsch. iabh wmzEneffco

  private String versorgungstyp; // EL>Liegenschaft> 030 Versorgungstyp

  private boolean tww; // if(wmzEneffco != 1) false
  // else if (versorgungstyp.toLowerCase().contains("tww")) true
  // else false

  private double utilizationRatePreviousWeek; // ESZ > 103 Nutzungsgrad Vorwoche

  // results
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

  public void calcTww() {
    if (wmzEneffco != 1) {
      this.tww = false;
    } else {
      this.tww = versorgungstyp.toLowerCase().contains("tww");
    }
  }

  public String toJson() {
    // TODO
    return this.toString();
  }

  // TODO: update with relevant values
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("code: " + code + "\n");
    str.append("wmzEneffco: " + wmzEneffco + "\n");
    str.append("vorlaufId: " + vorlaufId + "\n");
    str.append("ruecklaufId: " + ruecklaufId + "\n");
    str.append("volumenstromId: " + volumenstromId + "\n");
    str.append("auslastungKgrId: " + auslastungKgrId + "\n");
    str.append("aussentemperaturCode: " + aussentemperaturCode + "\n");
    str.append("leistungId: " + leistungId + "\n");
    str.append("versorgungstyp: " + versorgungstyp + "\n");
    str.append("tww: " + tww + "\n");
    str.append("textFragmentsId: " + textFragmentsId + "\n");
    str.append("textFragmentsPrevId: " + textFragmentsPrevId + "\n");
    str.append("textFragments: " + textFragments + "\n");
    str.append("einsparzaehlerObjectId: " + einsparzaehlerObjectId + "\n");
    str.append("wmzEneffco: " + wmzEneffco + "\n");
    str.append("einsparzaehlerObjectId: " + einsparzaehlerObjectId + "\n");
    str.append("liegenschaftObjectId: " + liegenschaftObjectId + "\n");
    str.append("regelparameterSollWerteObjectId: " + regelparameterSollWerteObjectId + "\n");
    return str.toString();
  }
}
