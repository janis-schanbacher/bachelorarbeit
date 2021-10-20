package com.ewus.ba.analysisService.model;

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

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();

    str.append("value: " + value + "\n");
    str.append("from: " + from + "\n");
    str.append("to: " + to + "\n");
    return str.toString();
  }
}
