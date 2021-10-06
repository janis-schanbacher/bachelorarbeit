package com.ewus.ba.analysisService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "facility_analysis_configurations")
public class FacilityAnalysisConfiguration {

  @Id private String id; // equals facilityCode;

  @Column(name = "Anlagengroesse")
  private boolean facilitySize;

  @Column(name = "Nutzungsgrad")
  private boolean utilizationRate;

  @Column(name = "Temperaturdifferenz")
  private boolean deltaTemperature;

  @Column(name = "Ruecklauftemperatur")
  private boolean returnTemperature;

  /** Constructor */
  public FacilityAnalysisConfiguration() {}

  /**
   * Constructor that sets all arguments
   *
   * @param id Id, code of a facility in format ABC.123
   * @param facilitySize
   * @param utilizationRate
   * @param deltaTemperature
   * @param returnTemperature
   */
  public FacilityAnalysisConfiguration(
      String id,
      boolean facilitySize,
      boolean utilizationRate,
      boolean deltaTemperature,
      boolean returnTemperature) {
    this.id = id;
    this.facilitySize = facilitySize;
    this.utilizationRate = utilizationRate;
    this.deltaTemperature = deltaTemperature;
    this.returnTemperature = returnTemperature;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean getFacilitySize() {
    return facilitySize;
  }

  public void setFacilitySize(boolean facilitySize) {
    this.facilitySize = facilitySize;
  }

  public boolean getUtilizationRate() {
    return utilizationRate;
  }

  public void setUtilizationRate(boolean utilizationRate) {
    this.utilizationRate = utilizationRate;
  }

  public boolean getDeltaTemperature() {
    return deltaTemperature;
  }

  public void setDeltaTemperature(boolean deltaTemperature) {
    this.deltaTemperature = deltaTemperature;
  }

  public boolean getReturnTemperature() {
    return returnTemperature;
  }

  public void setReturnTemperature(boolean returnTemperature) {
    this.returnTemperature = returnTemperature;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof FacilityAnalysisConfiguration) {
      FacilityAnalysisConfiguration config = (FacilityAnalysisConfiguration) obj;

      return id.equals(config.getId())
          && facilitySize == config.getFacilitySize()
          && utilizationRate == config.getUtilizationRate()
          && deltaTemperature == config.getDeltaTemperature()
          && returnTemperature == config.getReturnTemperature();
    }
    return false;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append("id: " + id + "\n");
    str.append("facilitySize: " + facilitySize + "\n");
    str.append("utilizationRate: " + utilizationRate + "\n");
    str.append("deltaTemperature: " + deltaTemperature + "\n");
    str.append("returnTemperature: " + returnTemperature + "\n");
    return str.toString();
  }
}
