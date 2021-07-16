package com.ewus.ba.analysisService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "analysisConfiguration")
public class Configuration {
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    private boolean isAnalysis1Active;
    private boolean isAnalysis2Active;

    // private String[] enabledAnalysises;

    public Configuration() {
    }

    public Configuration(String code, boolean isAnalysis1Active, boolean isAnalysis2Active) {
        this.code = code;
        // this.enabledAnalysises = enabledAnalysises;
        this.isAnalysis1Active = isAnalysis1Active;
        this.isAnalysis2Active = isAnalysis2Active;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean getIsAnalysis1Active() {
        return this.isAnalysis1Active;
    }

    public void setIsAnalysis1Active(boolean isAnalysis1Active) {
        this.isAnalysis1Active = isAnalysis1Active;
    }

    public boolean getIsAnalysis2Active() {
        return this.isAnalysis2Active;
    }

    public void setIsAnalysis2Active(boolean isAnalysis2Active) {
        this.isAnalysis2Active = isAnalysis2Active;
    }
}
