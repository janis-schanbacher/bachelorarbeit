package com.ewus.ba.energielenkerEneffcoService.model;

public class EneffcoValue {
  private double value;
  private String from;
  private String to;

  public EneffcoValue() {}

  public EneffcoValue(double value, String from, String to) {
    this.value = value;
    this.from = from;
    this.to = to;
  }

  public double getValue() {
    return this.value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public String getFrom() {
    return this.from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return this.to;
  }

  public void setTo(String to) {
    this.to = to;
  }
}
