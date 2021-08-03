// package com.ewus.ba.energielenkerService.model;

// import org.json.JSONObject;
// public class ElObject {
//     private String code; // Code der Anlage, z.b. STO_001
//     private String einsparzaehlerobjektid;
//     private String liegenschaftObjektId;

    
    
//     // TODO:
//     // - Add Variables for Analysis-Values
//     // - Add Variables for EL-ids (Felder der Textbausteine)
//     private String nighttimeFrom; // Einsparzaehlerprotokoll> 040 Nachtabsenkung Start; z.b. "01:30",
//     private String nighttimeTo; // Einsparzaehlerprotokoll> 041 Nachtabsenkung Ende; z.b. "02:30"
    


//     public String getCode() {
//         return code;
//     }

//     public void setCode(String code) {
//         this.code = code;
//     }

//     public String getEinsparzaehlerobjektid() {
//         return einsparzaehlerobjektid;
//     }

//     public void setEinsparzaehlerobjektid(String einsparzaehlerobjektid) {
//         this.einsparzaehlerobjektid = einsparzaehlerobjektid;
//     }

//     public String getLiegenschaftObjektId() {
//         return liegenschaftObjektId;
//     }

//     public void setLiegenschaftObjektId(String liegenschaftObjektId) {
//         this.liegenschaftObjektId = liegenschaftObjektId;
//     }

//     public String getNighttimeFrom() {
//         return nighttimeFrom;
//     }

//     public void setNighttimeFrom(String nighttimeFrom) {
//         this.nighttimeFrom = nighttimeFrom;
//     }

//     public String getNighttimeTo() {
//         return nighttimeTo;
//     }

//     public void setNighttimeTo(String nighttimeTo) {
//         this.nighttimeTo = nighttimeTo;
//     }

//     public String toString() {
//         StringBuilder str = new StringBuilder();
//         str.append("code: " + code + "\n"); // Code der Anlage, z.b. STO_001
//         str.append("einsparzaehlerobjektid: " + einsparzaehlerobjektid + "\n");
//         str.append("liegenschaftObjektId: " + liegenschaftObjektId + "\n");
//         return str.toString();
//     }

//     public JSONObject toJson() {
//         JSONObject json = new JSONObject();
//         System.out.println("code: " + this.code);
//         json.put("code", this.code);
//         json.put("einsparzaehlerobjektid", this.einsparzaehlerobjektid);
//         json.put("liegenschaftObjektId", this.liegenschaftObjektId);
//         return json;
//     }
// }
// TODO cleanup

package com.ewus.ba.energielenkerService.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class ElObject {

    private String code; // Code der Anlage, z.b. STO_001
    private int nachtabsenkungAbfrageintervall; // Einsparzaehlerprotokoll > 200 Nachtabsenkung Abfrageintervall (Tage)
    private int nachtabsenkungAbfragezeitraum; // Einsparzaehlerprotokoll > 201 Nachtabsenkung Abfragezeitraum (Tage)
    
   
    private int wmzEneffco; // Einsparzaehlerprotokoll> 190 WMZ Eneffco (letzte Stelle im DP Code)
    // vorlauf, aussentemperatur, volumenstrom and leistung are eneffco object ids
    private String vorlauf; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VL.[wmzEneffco]
                            // z.b. "4a3a0973-2fc4-47b6-aaf7-1ddd52bc94af"
    private String ruecklauf; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.RL.[wmzEneffco]
    private String volumenstrom;// Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VS.[wmzEneffco]
    private String leistung; // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.L.[wmzEneffco]
    private String versorgungstyp; // EL>Liegenschaft> 030 Versorgungstyp
    private String aussentemperaturCode; // EL>Liegenschaft> 038 zugeordnete Außentemperatur
    private String aussentemperatur; // Eneffco Datenpunkt Tabelle: [aussentemperaturCode]}
    private boolean tww; // if(wmzEneffco != 1) false
                         // else if (versorgungstyp.toLowerCase().contains("tww")) true
                         // else false
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

    public ElObject(String code) {
        this.code = code;
    }

    public ElObject() {
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

    public String getVorlauf() {
        return vorlauf;
    }

    public void setVorlauf(String vorlauf) {
        this.vorlauf = vorlauf;
    }

    public String getRuecklauf() {
        return ruecklauf;
    }

    public void setRuecklauf(String ruecklauf) {
        this.ruecklauf = ruecklauf;
    }

    public String getAussentemperaturCode() {
        return aussentemperaturCode;
    }

    public void setAussentemperaturCode(String aussentemperaturCode) {
        this.aussentemperaturCode = aussentemperaturCode;
    }

    public String getAussentemperatur() {
        return aussentemperatur;
    }

    public void setAussentemperatur(String aussentemperatur) {
        this.aussentemperatur = aussentemperatur;
    }

    public String getVolumenstrom() {
        return volumenstrom;
    }

    public void setVolumenstrom(String volumenstrom) {
        this.volumenstrom = volumenstrom;
    }

    public String getLeistung() {
        return leistung;
    }

    public void setLeistung(String leistung) {
        this.leistung = leistung;
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
        // vorlauf, aussentemperatur, volumenstrom and leistung are eneffco object ids
        str.append("vorlauf: " + vorlauf + "\n"); // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VL.[wmzEneffco]
        // z.b. "4a3a0973-2fc4-47b6-aaf7-1ddd52bc94af"
        str.append("ruecklauf: " + ruecklauf + "\n"); // Eneffco Datenpunkt Tabelle:
                                                      // [Anlagencode].WEZ.WMZ.RL.[wmzEneffco]
        str.append("aussentemperaturCode: " + aussentemperaturCode + "\n");
        str.append("aussentemperatur: " + aussentemperatur + "\n"); // EL>Liegenschaft> 038 zugeordnete Außentemperatur
        str.append("volumenstrom: " + volumenstrom + "\n");// Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.VS.
        str.append("leistung: " + leistung + "\n"); // Eneffco Datenpunkt Tabelle: [Anlagencode].WEZ.WMZ.L.[wmzEneffco]
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

