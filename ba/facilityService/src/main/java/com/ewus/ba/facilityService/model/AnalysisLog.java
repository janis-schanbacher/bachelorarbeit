package com.ewus.ba.facilityService.model;

import java.time.temporal.ChronoUnit;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.apache.commons.lang.StringUtils;

@Entity
@Table(name = "facility_analysis_logs")
public class AnalysisLog {
  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "anlagencode")
  private String code;

  @Column(name = "textbausteine_analyseergebnis", length=8000)
  private String textFragmentsAnalysisResult;

  @Column(name = "textbausteine_gespeichert", length=8000)
  private String textFragments;

  @Column(name = "textbausteine_unterschied", length=8000)
  private String textFragmentsDiff;

  @Column(name = "zeitstempel")
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

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
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
    this.textFragmentsDiff = "Added: " + StringUtils.difference(this.textFragmentsAnalysisResult, this.textFragments)
        + "Deleted: " + StringUtils.difference(this.textFragments, this.textFragmentsAnalysisResult);
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("id: " + id + "\n");
    str.append("code: " + code + "\n");
    str.append("textFragmentsAnalysisResult: " + textFragmentsAnalysisResult + "\n");
    str.append("textFragments: " + textFragments + "\n");
    str.append("textFragmentsDiff: " + textFragmentsDiff + "\n");
    str.append("timestamp: " + timestamp + "\n");
    return str.toString();
  }
}
