package com.ewus.ba.facilityService.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AnalysisLogTest {
  AnalysisLog log;

  @BeforeEach
  public void setup() {
    log = new AnalysisLog("TST.001", "textFragments generated", "textFragments edited");
    log.setTimestamp("2021-10-19T08:19:59.171Z");
  }

  @Test
  void testCode() {
    assertEquals("TST.001", log.getCode());
  }

  @Test
  void testGetTextFragmentsAnalysisResult() {
    assertEquals("textFragments generated", log.getTextFragmentsAnalysisResult());
  }

  @Test
  void testSetTextFragmentsAnalysisResult() {
    log.setTextFragmentsAnalysisResult("textFragments generated v2");
    assertEquals("textFragments generated v2", log.getTextFragmentsAnalysisResult());
  }

  @Test
  void testGetTextFragments() {
    assertEquals("textFragments edited", log.getTextFragments());
  }

  @Test
  void testSetTextFragments() {
    log.setTextFragments("textFragments edited v2");
    assertEquals("textFragments edited v2", log.getTextFragments());
  }

  @Test
  void testGetTimestamp() {
    assertEquals("2021-10-19T08:19:59.171Z", log.getTimestamp());
  }

  @Test
  void testSetTimestamp() {
    log.setTimestamp("2021-10-19T09:00:00.171Z");
    assertEquals("2021-10-19T09:00:00.171Z", log.getTimestamp());
  }

  @Test
  void testGetTextFragmentsDiff() {
    assertEquals("Added: edited, Deleted: generated", log.getTextFragmentsDiff());
  }

  @Test
  void testCalcDiffAnalysisEdited() {
    log.setTextFragmentsAnalysisResult("TextFragments generated");
    log.setTextFragments("TextFragments edited");
    log.calcDiffAnalysisEdited();
    assertEquals("Added: edited, Deleted: generated", log.getTextFragmentsDiff());
  }
}
