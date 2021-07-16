package com.ewus.ba.energielenkerService.model;

public class ElObject {
    private String code; // Code der Anlage, z.b. STO_001
    private String einsparzaehlerobjektid;
    private String liegenschaftObjektId;

    // TODO:
    // - Add Variables for Analysis-Values
    // - Add Variables for EL-ids (Felder der Textbausteine)

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEinsparzaehlerobjektid() {
        return einsparzaehlerobjektid;
    }

    public void setEinsparzaehlerobjektid(String einsparzaehlerobjektid) {
        this.einsparzaehlerobjektid = einsparzaehlerobjektid;
    }

    public String getLiegenschaftObjektId() {
        return liegenschaftObjektId;
    }

    public void setLiegenschaftObjektId(String liegenschaftObjektId) {
        this.liegenschaftObjektId = liegenschaftObjektId;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("code: " + code + "\n"); // Code der Anlage, z.b. STO_001
        str.append("einsparzaehlerobjektid: " + einsparzaehlerobjektid + "\n");
        str.append("liegenschaftObjektId: " + liegenschaftObjektId + "\n");
        return str.toString();
    }
}
