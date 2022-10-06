package com.ewus.ba.analysisService.model;

import com.ewus.ba.analysisService.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;

public class Facility {

  private String code; // Code der Anlage, z.b. TST_001

  private int wmzEneffco; // Einsparzaehlerprotokoll> 190 WMZ Eneffco (letzte Stelle im
  // DP Code)

  private String vorlaufId; // Eneffco Datenpunkt Tabelle:
  // [Anlagencode].WEZ.WMZ.VL.[wmzEneffco]

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

  static final ObjectMapper objectMapper = new ObjectMapper();
  private static final OkHttpClient client =
      new OkHttpClient()
          .newBuilder()
          .connectTimeout(10, TimeUnit.SECONDS)
          .readTimeout(60, TimeUnit.SECONDS)
          .build();

  static final int TIMEINTERVAL_15M = 900;
  static final int TIMEINTERVAL_DAY = 86400;
  static final int TIMEINTERVAL_WEEK = 86407;

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
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return "";
    }
  }

  // Analysis logic
  public List<String> analayse(FacilityAnalysisConfiguration config) {
    List<String> textFragments = new ArrayList<>();

    if (config.getFacilitySize()) {
      textFragments.add(analyseFacilitySize());
    }
    if (config.getUtilizationRate()) {
      textFragments.add(analyseUtilizationRate());
    }
    if (config.getDeltaTemperature()) {
      textFragments.add(analyseDeltaTemperature());
    }
    if (config.getReturnTemperature()) {
      textFragments.add(analyseReturnTemperature());
    }

    textFragments.removeAll(Arrays.asList("", null));

    // Add previous textfragments
    textFragments.add("prev: " + getTextFragments());

    return textFragments;
  }

  public String analyseFacilitySize() {
    List<EneffcoValue> values =
        getEneffcoValues(
            getAuslastungKgrId(),
            java.time.Clock.systemUTC()
                .instant()
                .truncatedTo(ChronoUnit.MILLIS)
                .minus(365, ChronoUnit.DAYS)
                .toString(),
            java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString(),
            TIMEINTERVAL_15M,
            false);

    if (values == null) {
      Utils.LOGGER.warn(
          getCode()
              + ": Could not retrieve Eneffco values from Eneffco-Datapoint: .WEZ.AUS.MAX."
              + getWmzEneffco());
      return "Anlagengröße konnte nicht analysisert werden. Bitte zugehörigen Eneffco-Datenpunkt ("
          + getCode()
          + ".WEZ.AUS.MAX."
          + getWmzEneffco()
          + ") überprüfen.";
    }

    float avgAuslastungKgr = getAverageValue(values);
    String textFragment =
        avgAuslastungKgr > 80
            ? ""
            : "Heizkessel ist überdimensioniert, Durchschnittliche Auslastung "
                + String.format("%.2f", avgAuslastungKgr)
                + "%. Mögliche Maßnahmen: Brenner einstellen, andere Düse verbauen (geringere Heizleistung) oder Neubau.";
    return textFragment;
  }

  public String analyseUtilizationRate() {
    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();
    int currentDay = LocalDate.now().getDayOfMonth();
    String from, to;
    if (currentMonth <= 3) { // First months of year
      from =
          LocalDateTime.of(currentYear - 1, Month.NOVEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
    } else if (currentMonth >= 11 && currentDay > 14) { // at least 2 weeks in timesspan
      from =
          LocalDateTime.of(currentYear, Month.NOVEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();

    } else {
      from =
          LocalDateTime.of(currentYear - 1, Month.NOVEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to =
          LocalDateTime.of(currentYear, Month.MARCH, 28, 23, 59, 59)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
    }

    List<EneffcoValue> values =
        getEneffcoValues(getNutzungsgradId(), from, to, TIMEINTERVAL_DAY, false);

    if (values == null) {
      return "Nutzungsgrad konnte nicht analysiert werden. Bitte zugehörigen Eneffco-Datenpunkt ("
          + getCode()
          + ".WEZ.ETA."
          + getWmzEneffco()
          + ") überprüfen.";
    }
    float avgUtilizationRate = getAverageValue(values);
    if (avgUtilizationRate == 0) {
      return "Nutzungsgrad konnte nicht analysiert werden. Bitte zugehörigen Eneffco-Datenpunkt ("
          + getCode()
          + ".WEZ.ETA."
          + getWmzEneffco()
          + ") überprüfen.";
    }

    String textFragment = "";

    final double LIMIT_UTILIZATION_RATE = getBrennwertkessel() ? 90 : 80;
    if (avgUtilizationRate < LIMIT_UTILIZATION_RATE) {
      textFragment =
          "Die Anlage weist einen zu geringen Nutzungsgrad auf (Avg. Nutzungsgrad: "
              + String.format("%.2f", avgUtilizationRate)
              + "%). Maßnahmen: Prüfen ob WMZ Gesamt gemessen wird, Anlagenanalyse durchführen.​";
    } else if (getUtilizationRatePreviousWeek() != 0
        && getUtilizationRatePreviousWeek() < LIMIT_UTILIZATION_RATE) {
      textFragment =
          "Die Anlage weist einen zu geringen Nutzungsgrad auf (Nutzungsgrad Vorwoche: "
              + String.format("%.2f", getUtilizationRatePreviousWeek())
              + "%). Maßnahmen: Prüfen ob WMZ Gesamt gemessen wird, Anlagenanalyse durchführen.";
    }

    return textFragment;
  }

  public String analyseDeltaTemperature() {
    int currentYear = LocalDate.now().getYear();
    String from =
        LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0)
            .atZone(ZoneId.of("Europe/Berlin"))
            .toInstant()
            .toString();
    String to =
        LocalDateTime.of(currentYear, Month.FEBRUARY, 28, 23, 59, 59)
            .atZone(ZoneId.of("Europe/Berlin"))
            .toInstant()
            .toString();

    List<EneffcoValue> values =
        getEneffcoValues(getDeltaTemperatureId(), from, to, TIMEINTERVAL_15M, false);

    if (values == null) {
      return "Temperaturdifferenz konnte nicht analysiert werden. Bitte zugehörigen Eneffco-Datenpunkt ("
          + getCode()
          + ".WEZ.WMZ.DT."
          + getWmzEneffco()
          + ") überprüfen.";
    }
    float avgDeltaTemperature = getAverageValue(values);
    String textFragment = "";
    if (getBrennwertkessel()) {
      if (avgDeltaTemperature < 15) {
        textFragment =
            "Die Temperaturspreizung ist mit "
                + String.format("%.2f", avgDeltaTemperature)
                + " K zu gering. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
      }
    } else if (avgDeltaTemperature < 10) {
      textFragment =
          "Die Temperaturspreizung ist mit "
              + String.format("%.2f", avgDeltaTemperature)
              + " K zu gering. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
    }

    return textFragment;
  }

  public String analyseReturnTemperature() {
    // Analysis is ment for facilities with Brennwertkessel
    if (getBrennwertkessel() == false) {
      return "";
    }
    int currentYear = LocalDate.now().getYear();
    int currentMonth = LocalDate.now().getMonthValue();
    int currentDay = LocalDate.now().getDayOfMonth();
    String from, to;
    if (currentMonth <= 2) { // First months of year
      from =
          LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
    } else if (currentMonth >= 12 && currentDay > 14) { // at least 2 weeks in timesspan
      from =
          LocalDateTime.of(currentYear, Month.DECEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to = java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();

    } else {
      from =
          LocalDateTime.of(currentYear - 1, Month.DECEMBER, 1, 0, 0, 0)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
      to =
          LocalDateTime.of(currentYear, Month.FEBRUARY, 28, 23, 59, 59)
              .atZone(ZoneId.of("Europe/Berlin"))
              .toInstant()
              .toString();
    }

    List<EneffcoValue> values =
        getEneffcoValues(getRuecklaufId(), from, to, TIMEINTERVAL_DAY, false);

    if (values == null) {
      return "Rücklauftemperatur konnte nicht analysiert werden. Bitte zugehörigen Eneffco-Datenpunkt ("
          + getCode()
          + ".WEZ.WMZ.RL."
          + getWmzEneffco()
          + ") überprüfen.";
    }

    final double LIMIT_PORTION_ACCEPTED_MIN = 90.0 / 100;
    final double LIMIT_RETURN_TEMPERATURE = 55;
    double portionOfValuesMarginBelowLimit =
        getPortionOfValuesMargin(values, LIMIT_RETURN_TEMPERATURE);

    String textFragment = "";
    if (portionOfValuesMarginBelowLimit < LIMIT_PORTION_ACCEPTED_MIN) {
      textFragment =
          "Brennwerteffekt wird nicht ausreichend genutzt. Maßnahmen: Heizkurve einstellen, Absenkung VL-Temp., Verringerung der Wasserumlaufmenge.";
    }
    return textFragment;
  }

  public List<EneffcoValue> getEneffcoValues(
      String datapointId, String from, String to, int timeInterval, boolean includeNanValues) {
    String baseUrl = "http://localhost:8080/eneffco/datapoint/";
    HttpUrl.Builder httpBuilder = HttpUrl.parse(baseUrl + datapointId + "/values").newBuilder();
    httpBuilder.addQueryParameter("from", from);
    httpBuilder.addQueryParameter("to", to);
    httpBuilder.addQueryParameter("timeInterval", String.valueOf(timeInterval));
    httpBuilder.addQueryParameter("includeNanValues", String.valueOf(includeNanValues));

    Request request = new Request.Builder().url(httpBuilder.build()).build();
    Response response = null;
    String responseBody = null;
    try {
      response = client.newCall(request).execute();

      if (response.code() == 400 || response.code() == 404 || response.code() == 500) {
        throw new Exception(
            "Response get "
                + baseUrl
                + datapointId
                + "/values : "
                + response.code()
                + ". Error occured while analysing facility: "
                + getCode());
      }

      responseBody = response.body().string();
      JSONArray jArray = new JSONArray(responseBody);
      List<EneffcoValue> values = new ArrayList<>();

      for (int i = 0; i < jArray.length(); i++) {
        values.add(objectMapper.readValue(jArray.get(i).toString(), EneffcoValue.class));
      }
      return values;

    } catch (Exception e) {
      Utils.LOGGER.warn(
          "Failed to get Eneffco values from datapointId: "
              + datapointId
              + ", with parameters from: "
              + from
              + ", to: "
              + to
              + ", timeInterval: "
              + timeInterval
              + ",  includeNanValues: "
              + includeNanValues
              + ". \n error message: "
              + e.getMessage());
      return null;
    }
  }

  public float getAverageValue(List<EneffcoValue> values) {
    float sum = 0;
    for (int i = 0; i < values.size(); i++) {
      sum += values.get(i).getValue();
    }
    return sum / values.size();
  }

  public double getPortionOfValuesMargin(List<EneffcoValue> values, double limit) {
    int acceptedValuesCount = 0;
    for (int i = 0; i < values.size(); i++) {
      acceptedValuesCount += (values.get(i).getValue() < limit) ? 1 : 0;
    }
    return (double) acceptedValuesCount / values.size();
  }
}
