package com.ewus.ba.facilityService.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.temporal.ChronoUnit;
import org.apache.commons.lang3.StringUtils;

public class AnalysisLog {
  private String code;
  private String textFragmentsAnalysisResult;
  private String textFragments;
  private String textFragmentsDiff;
  private String timestamp;

  /** Constructor */
  public AnalysisLog() {}

  public AnalysisLog(String code, String textFragmentsAnalysisResult, String textFragments) {
    this.code = code;
    this.textFragmentsAnalysisResult = textFragmentsAnalysisResult;
    this.textFragments = textFragments;
    this.calcDiffAnalysisEdited();
    this.timestamp =
        java.time.Clock.systemUTC().instant().truncatedTo(ChronoUnit.MILLIS).toString();
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getTextFragmentsAnalysisResult() {
    return this.textFragmentsAnalysisResult;
  }

  public void setTextFragmentsAnalysisResult(String textFragmentsAnalysisResult) {
    this.textFragmentsAnalysisResult = textFragmentsAnalysisResult;
  }

  public String getTextFragments() {
    return this.textFragments;
  }

  public void setTextFragments(String textFragments) {
    this.textFragments = textFragments;
  }

  public String getTextFragmentsDiff() {
    return this.textFragmentsDiff;
  }

  public void setTextFragmentsDiff(String textFragmentsDiff) {
    this.textFragmentsDiff = textFragmentsDiff;
  }

  public String getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public void calcDiffAnalysisEdited() {
    this.textFragmentsDiff =
        "Added: "
            + StringUtils.difference(this.textFragmentsAnalysisResult, this.textFragments)
            + ", Deleted: "
            + StringUtils.difference(this.textFragments, this.textFragmentsAnalysisResult);
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
