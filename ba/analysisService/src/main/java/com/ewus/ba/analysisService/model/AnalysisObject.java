package com.ewus.ba.analysisService.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "analysisObjects")
public class AnalysisObject {
    @Id
    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "textblocks")
    private String[] textblocks;
    @Column(name = "textblocksPrevious")
    private String[] textblocksPrevious;
    // TODO: maybe use enum istead: open, written, modifiedAndWritten, Rejected,
    // diffModifiedPrev
    @Column(name = "state")
    private String state;

    public AnalysisObject() {
    }

    public AnalysisObject(String code, String[] textblocks, String[] textblocksPrevious, String state) {
        this.code = code;
        this.textblocks = textblocks;
        this.textblocksPrevious = textblocksPrevious;
        this.state = state;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getTextblocks() {
        return this.textblocks;
    }

    public void setTextblocks(String[] textblocks) {
        this.textblocks = textblocks;
    }

    public String[] getTextblocksPrevious() {
        return this.textblocksPrevious;
    }

    public void setTextblocksPrevious(String[] textblocksPrevious) {
        this.textblocksPrevious = textblocksPrevious;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Analysis Settings. TODO: if to much, source out to settings Object
    // TODO: fill with proper analysis names
    public boolean doAnalysis1;
    public boolean doAnalysis2;

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("code: " + code + "\n"); // Code der Anlage, z.b. STO_001
        str.append("textblocks: " + textblocks + "\n");
        str.append("textblocksPrevious: " + textblocksPrevious + "\n");
        str.append("state: " + state + "\n");
        return str.toString();
    }
}
