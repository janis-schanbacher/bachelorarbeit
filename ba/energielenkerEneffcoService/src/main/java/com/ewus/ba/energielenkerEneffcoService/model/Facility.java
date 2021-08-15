
//     // TODO:
//     // - Add Variables for Analysis-Values
//     // - Add Variables for EL-ids (Felder der Textbausteine)
//     private String nighttimeFrom; // Einsparzaehlerprotokoll> 040 Nachtabsenkung Start; z.b. "01:30",
//     private String nighttimeTo; // Einsparzaehlerprotokoll> 041 Nachtabsenkung Ende; z.b. "02:30"

package com.ewus.ba.energielenkerEneffcoService.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class Facility {

    private String code; // Code der Anlage, z.b. STO_001
    private int nachtabsenkungAbfrageintervall; // Einsparzaehlerprotokoll > 200 Nachtabsenkung Abfrageintervall (Tage)
    private int nachtabsenkungAbfragezeitraum; // Einsparzaehlerprotokoll > 201 Nachtabsenkung Abfragezeitraum (Tage)

    private int wmzEneffco; // Einsparzaehlerprotokoll> 190 WMZ Eneffco (letzte Stelle im DP Code)
    // vorlaufId, aussentemperatur, volumenstromId and leistungId are eneffco object
    // ids
    private String vorlaufId; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VL.[wmzEneffco]
                              // z.b. "4a3a0973-2fc4-47b6-aaf7-1ddd52bc94af"
    private String ruecklaufId; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.RL.[wmzEneffco]
    private String volumenstromId;// Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VS.[wmzEneffco]
    private String leistungId; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.L.[wmzEneffco]
    private String aussentemperaturId; // Eneffco Datenpunkt Tabelle: [aussentemperaturCode]}
    private String nutzungsgradId; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.ETA.[wmzEneffco]
    private String auslastungKgrId; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.AUS.KGR.[wmzEneffco]
    private String deltaTemperatureId; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.DT.[wmzEneffco]

    private String aussentemperaturCode; // EL>Liegenschaft> 038 zugeordnete Außentemperatur
    private boolean brennwertkessel; // EL>Anlagentechnik> 011 Brennwertkessel
    // TODO 021 Brennwertkessel berücksichtigen, wahrsch. iabh wmzEneffco

    private String versorgungstyp; // EL>Liegenschaft> 030 Versorgungstyp
    private boolean tww; // if(wmzEneffco != 1) false
                         // else if (versorgungstyp.toLowerCase().contains("tww")) true
                         // else false

    private double utilizationRatePreviousWeek; // ESZ > 103 Nutzungsgrad Vorwoche
    private String nighttimeFrom; // Einsparzaehlerprotokoll> 040 Nachtabsenkung Start; z.b. "01:30",
    private String nighttimeTo; // Einsparzaehlerprotokoll> 041 Nachtabsenkung Ende; z.b. "02:30"
    // results
    private String absenkungWeekStart; // Einsparzaehlerprotokoll> 040 Nachtabsenkung Start
    private String absenkungWeekEnd; // Einsparzaehlerprotokoll> 041 Nachtabsenkung Ende
    private String absenkungWeekendStart; // Einsparzaehlerprotokoll> 042 Nachtabsenkung WE Start
    private String absenkungWeekendEnd; // Einsparzaehlerprotokoll> 043 Nachtabsenkung WE Ende
    private String pAbsenkungMoDiMiDo; // Einsparzaehlerprotokoll> 311 Nachtabsenkung Wahrsch. Mo-Do
    private String pAbsenkungFr; // Einsparzaehlerprotokoll> 311 Nachtabsenkung Wahrsch. Fr
    private String pAbsenkungSa; // Einsparzaehlerprotokoll> 312 Nachtabsenkung Wahrsch. Sa
    private String pAbsenkungSo; // Einsparzaehlerprotokoll> 313 Nachtabsenkung Wahrsch. So
    int[] predictionsUmstellung; // in Rohdatenpunkt ACO.001.WEZ.MON.1 in Eneffco schreiben
    private String heizgrenze; // Einsparzaehlerprotokoll> 050 Heizgrenze
    private String heizgrenzePumpe; // Einsparzaehlerprotokoll> 051 Heizgrenze Pumpe
    private String yAchsenabschnitt; // Einsparzaehlerprotokoll> 300 Heizkurve y-Achsenabschnitt
    private String steigung; // Einsparzaehlerprotokoll> 301 Heizkurve Steigung
    private String minimumAussentemp; // Einsparzaehlerprotokoll> 302 Heizkurve minimumAussentemp

    private String einsparzaehlerobjektid;
    private String liegenschaftObjektId;

    public Facility(String code) {
        this.code = code;
    }

    public Facility() {
    }

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

    public String getAussentemperaturId() {
        return aussentemperaturId;
    }

    public void setAussentemperaturId(String aussentemperaturId) {
        this.aussentemperaturId = aussentemperaturId;
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

    public String getDeltaTemeratureId() {
        return deltaTemperatureId;
    }

    public void setDeltaTemeratureId(String deltaTemperatureId) {
        this.deltaTemperatureId = deltaTemperatureId;
    }

    public String getVolumenstromId() {
        return volumenstromId;
    }

    public void setVolumenstromId(String volumenstromId) {
        this.volumenstromId = volumenstromId;
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

    public String getNighttimeFrom() {
        return nighttimeFrom;
    }

    public void setNighttimeFrom(String nighttimeFrom) {
        this.nighttimeFrom = nighttimeFrom;
    }

    public double getUtilizationRatePreviousWeek() {
        return utilizationRatePreviousWeek;
    }

    public void setUtilizationRatePreviousWeek(double utilizationRatePreviousWeek) {
        this.utilizationRatePreviousWeek = utilizationRatePreviousWeek;
    }

    public String getNighttimeTo() {
        return nighttimeTo;
    }

    public void setNighttimeTo(String nighttimeTo) {
        this.nighttimeTo = nighttimeTo;
    }

    public String getAbsenkungWeekStart() {
        return absenkungWeekStart;
    }

    public void setAbsenkungWeekStart(String absenkungWeekStart) {
        this.absenkungWeekStart = absenkungWeekStart;
    }

    public String getAbsenkungWeekEnd() {
        return absenkungWeekEnd;
    }

    public void setAbsenkungWeekEnd(String absenkungWeekEnd) {
        this.absenkungWeekEnd = absenkungWeekEnd;
    }

    public String getAbsenkungWeekendStart() {
        return absenkungWeekendStart;
    }

    public void setAbsenkungWeekendStart(String absenkungWeekendStart) {
        this.absenkungWeekendStart = absenkungWeekendStart;
    }

    public String getAbsenkungWeekendEnd() {
        return absenkungWeekendEnd;
    }

    public void setAbsenkungWeekendEnd(String absenkungWeekendEnd) {
        this.absenkungWeekendEnd = absenkungWeekendEnd;
    }

    public String getpAbsenkungMoDiMiDo() {
        return pAbsenkungMoDiMiDo;
    }

    public void setpAbsenkungMoDiMiDo(String pAbsenkungMoDiMiDo) {
        this.pAbsenkungMoDiMiDo = pAbsenkungMoDiMiDo;
    }

    public String getpAbsenkungFr() {
        return pAbsenkungFr;
    }

    public void setpAbsenkungFr(String pAbsenkungFr) {
        this.pAbsenkungFr = pAbsenkungFr;
    }

    public String getpAbsenkungSa() {
        return pAbsenkungSa;
    }

    public void setpAbsenkungSa(String pAbsenkungSa) {
        this.pAbsenkungSa = pAbsenkungSa;
    }

    public String getpAbsenkungSo() {
        return pAbsenkungSo;
    }

    public void setpAbsenkungSo(String pAbsenkungSo) {
        this.pAbsenkungSo = pAbsenkungSo;
    }

    public int[] getPredictionsUmstellung() {
        return predictionsUmstellung;
    }

    public void setPredictionsUmstellung(int[] predictionsUmstellung) {
        this.predictionsUmstellung = predictionsUmstellung;
    }

    public String getHeizgrenze() {
        return heizgrenze;
    }

    public void setHeizgrenze(String heizgrenze) {
        this.heizgrenze = heizgrenze;
    }

    public String getHeizgrenzePumpe() {
        return heizgrenzePumpe;
    }

    public void setHeizgrenzePumpe(String heizgrenzePumpe) {
        this.heizgrenzePumpe = heizgrenzePumpe;
    }

    public String getyAchsenabschnitt() {
        return yAchsenabschnitt;
    }

    public void setyAchsenabschnitt(String yAchsenabschnitt) {
        this.yAchsenabschnitt = yAchsenabschnitt;
    }

    public String getSteigung() {
        return steigung;
    }

    public void setSteigung(String steigung) {
        this.steigung = steigung;
    }

    public String getMinimumAussentemp() {
        return minimumAussentemp;
    }

    public void setMinimumAussentemp(String minimumAussentemp) {
        this.minimumAussentemp = minimumAussentemp;
    }

    public String getEinsparzaehlerobjektid() {
        return einsparzaehlerobjektid;
    }

    public void setEinsparzaehlerobjektid(String einsparzaehlerobjektid) {
        this.einsparzaehlerobjektid = einsparzaehlerobjektid;
    }

    public String getVersorgungstyp() {
        return versorgungstyp;
    }

    public void setVersorgungstyp(String versorgungstyp) {
        this.versorgungstyp = versorgungstyp;
    }

    public String getLiegenschaftObjektId() {
        return liegenschaftObjektId;
    }

    public void setLiegenschaftObjektId(String liegenschaftObjektId) {
        this.liegenschaftObjektId = liegenschaftObjektId;
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
        str.append("code: " + code + "\n"); // Code der Anlage, z.b. STO_001
        // Nachtabsenkung
        // Abfrageintervall (Tage)
        str.append("nachtabsenkungAbfragezeitraum: " + nachtabsenkungAbfragezeitraum + "\n"); // Einsparzaehlerprotokoll
                                                                                              // > 201
        // Abfrage-Parameter from und to ...
        str.append("wmzEneffco: " + wmzEneffco + "\n"); // Einsparzaehlerprotokoll> 190 WMZ Eneffco (letzte Stelle im DP
                                                        // Code)
        // vorlaufId, aussentemperatur, volumenstromId and leistungId are eneffco object
        // ids
        str.append("vorlaufId: " + vorlaufId + "\n"); // Eneffco Datenpunkt Tabelle:
                                                      // [Anlagencode].WEZ.WMZ.VL.[wmzEneffco]
        // z.b. "4a3a0973-2fc4-47b6-aaf7-1ddd52bc94af"
        str.append("ruecklaufId: " + ruecklaufId + "\n"); // Eneffco Datenpunkt Tabelle:
        // [Anlagencode].WEZ.WMZ.RL.[wmzEneffco]
        str.append("aussentemperaturCode: " + aussentemperaturCode + "\n");
        str.append("aussentemperaturId: " + aussentemperaturId + "\n"); // EL>Liegenschaft> 038 zugeordnete
                                                                        // Außentemperatur
        str.append("volumenstromId: " + volumenstromId + "\n");// Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VS.
        str.append("leistungId: " + leistungId + "\n"); // Eneffco Datenpunkt Tabelle:
                                                        // [Anlagencode].WEZ.WMZ.L.[wmzEneffco]
        str.append("versorgungstyp: " + versorgungstyp + "\n"); // EL>Liegenschaft> 030 Versorgungstyp
        str.append("tww: " + tww + "\n"); // if(wmzEneffco != 1) false
        // else if (versorgungstyp.toLowerCase().contains("tww")) true
        // else false
        str.append("nighttimeFrom: " + nighttimeFrom + "\n"); // Einsparzaehlerprotokoll> 040 Nachtabsenkung Start; z.b.
        // "01:30",
        str.append("nighttimeTo: " + nighttimeTo + "\n"); // Einsparzaehlerprotokoll> 041 Nachtabsenkung Ende; z.b.
                                                          // "02:30"
        // results
        str.append("absenkungWeekStart: " + absenkungWeekStart + "\n"); // Einsparzaehlerprotokoll> 040 Nachtabsenkung
                                                                        // Start
        str.append("absenkungWeekEnd: " + absenkungWeekEnd + "\n"); // Einsparzaehlerprotokoll> 041 Nachtabsenkung Ende
        str.append("absenkungWeekendStart: " + absenkungWeekendStart + "\n"); // Einsparzaehlerprotokoll> 042
                                                                              // Nachtabsenkung WE
        // Start
        str.append("absenkungWeekendEnd: " + absenkungWeekendEnd + "\n"); // Einsparzaehlerprotokoll> 043 Nachtabsenkung
                                                                          // WE Ende
        str.append("pAbsenkungMoDiMiDo: " + pAbsenkungMoDiMiDo + "\n"); // Einsparzaehlerprotokoll> 311 Nachtabsenkung
                                                                        // Wahrsch.
        // Mo-Do
        str.append("pAbsenkungFr: " + pAbsenkungFr + "\n"); // Einsparzaehlerprotokoll> 311 Nachtabsenkung Wahrsch. Fr
        str.append("pAbsenkungSa: " + pAbsenkungSa + "\n"); // Einsparzaehlerprotokoll> 312 Nachtabsenkung Wahrsch. Sa
        str.append("pAbsenkungSo: " + pAbsenkungSo + "\n"); // Einsparzaehlerprotokoll> 313 Nachtabsenkung Wahrsch. So
        int[] predictionsUmstellung; // in Rohdatenpunkt ACO.001.WEZ.MON.1 in Eneffco schreiben
        str.append("heizgrenze: " + heizgrenze + "\n"); // Einsparzaehlerprotokoll> 050 Heizgrenze
        str.append("heizgrenzePumpe: " + heizgrenzePumpe + "\n"); // Einsparzaehlerprotokoll> 051 Heizgrenze Pumpe
        str.append("yAchsenabschnitt: " + yAchsenabschnitt + "\n"); // Einsparzaehlerprotokoll> 300 Heizkurve
                                                                    // y-Achsenabschnitt
        str.append("steigung: " + steigung + "\n"); // Einsparzaehlerprotokoll> 301 Heizkurve Steigung
        str.append("minimumAussentemp: " + minimumAussentemp + "\n"); // Einsparzaehlerprotokoll> 302 Heizkurve
        // minimumAussentemp
        str.append("einsparzaehlerobjektid: " + einsparzaehlerobjektid + "\n");
        str.append("nachtabsenkungAbfrageintervall: " + nachtabsenkungAbfrageintervall + "\n"); // Einsparzaehlerprotokoll
                                                                                                // > 200
        // Nachtabsenkung
        // Abfrageintervall (Tage)
        str.append("nachtabsenkungAbfragezeitraum: " + nachtabsenkungAbfragezeitraum + "\n");
        str.append("wmzEneffco: " + wmzEneffco + "\n");
        str.append("einsparzaehlerobjektid: " + einsparzaehlerobjektid + "\n");
        str.append("liegenschaftObjektId: " + liegenschaftObjektId + "\n");
        return str.toString();
    }
}
