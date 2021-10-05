package com.ewus.ba.analysisService.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacilityTest {
  Facility f;

  @BeforeEach
  public void setup() {
    f = new Facility();
    f.setCode("TST.001");
    f.setWmzEneffco(1);
    f.setVorlaufId("b1a35f44-abe1-4afc-82eb-372889775b29");
    f.setRuecklaufId("a20772af-076f-421e-9c64-a78b1ce5017b");
    f.setVolumenstromId("607f72ce-7172-40f3-a1de-c0305f3b482f");
    f.setLeistungId("ee669174-10db-4e40-9b67-6b88a15707ee");
    f.setNutzungsgradId("ab7407da-f066-40e6-a975-6288ffa5ddb3");
    f.setAuslastungKgrId("cd56cb68-f551-4eac-8db2-4ed8d1ac011f");
    f.setDeltaTemperatureId("3db141b3-5414-4dea-b696-51e89c74494e");
    f.setAussentemperaturCode("433_T");
    f.setVersorgungstyp("TWW + Heizung");
    f.setBrennwertkessel(true);
    f.setTww(true);
    f.setUtilizationRatePreviousWeek(90.5);
    f.setEinsparzaehlerobjektid("27249");
    f.setLiegenschaftObjectId("25355");
    f.setRegelparameterSollWerteObjectId("30362");
    f.setTextFragments("Textfragments..");
    f.setTextFragmentsPrev("Textfragments previous..");
  }

  @Test
  void testConstructor() {
    new Facility("TST.002");
    new Facility();
  }

  @Test
  void testGetWmzEneffco() {
    assertEquals(1, f.getWmzEneffco());
  }

  @Test
  void testGetVorlaufId() {
    assertEquals("b1a35f44-abe1-4afc-82eb-372889775b29", f.getVorlaufId());
  }

  @Test
  void testGetRuecklaufId() {
    assertEquals("a20772af-076f-421e-9c64-a78b1ce5017b", f.getRuecklaufId());
  }

  @Test
  void testGetAussentemperaturCode() {
    assertEquals("433_T", f.getAussentemperaturCode());
  }

  @Test
  void testGetBrennwertkessel() {
    assertEquals(true, f.getBrennwertkessel());
  }

  @Test
  void testGetNutzungsgradId() {
    assertEquals("ab7407da-f066-40e6-a975-6288ffa5ddb3", f.getNutzungsgradId());
  }

  @Test
  void testGetAuslastungKgrId() {
    assertEquals("cd56cb68-f551-4eac-8db2-4ed8d1ac011f", f.getAuslastungKgrId());
  }

  @Test
  void testGetDeltaTemperatureId() {
    assertEquals("3db141b3-5414-4dea-b696-51e89c74494e", f.getDeltaTemperatureId());
  }

  @Test
  void testGetVolumenstromId() {
    assertEquals("607f72ce-7172-40f3-a1de-c0305f3b482f", f.getVolumenstromId());
  }

  @Test
  void testGetLeistungId() {
    assertEquals("ee669174-10db-4e40-9b67-6b88a15707ee", f.getLeistungId());
  }

  @Test
  void testGetTww() {
    assertEquals(true, f.getTww());
  }

  @Test
  void testGetUtilizationRatePreviousWeek() {
    assertEquals(90.5, f.getUtilizationRatePreviousWeek());
  }

  @Test
  void testGetTextFragments() {
    assertEquals("Textfragments..", f.getTextFragments());
  }

  @Test
  void testGetTextFragmentsPrev() {
    assertEquals("Textfragments previous..", f.getTextFragmentsPrev());
  }

  @Test
  void testGetEinsparzaehlerobjektid() {
    assertEquals("27249", f.getEinsparzaehlerobjektid());
  }

  @Test
  void testGetVersorgungstyp() {
    assertEquals("TWW + Heizung", f.getVersorgungstyp());
  }

  @Test
  void testGetLiegenschaftObjectId() {
    assertEquals("25355", f.getLiegenschaftObjectId());
  }

  @Test
  void testGetRegelparameterSollWerteObjectId() {
    assertEquals("30362", f.getRegelparameterSollWerteObjectId());
  }

  @Test
  void testSetWmzEneffco() {
    f.setWmzEneffco(2);
    assertEquals(2, f.getWmzEneffco());
  }

  @Test
  void testSetVorlaufId() {
    f.setVorlaufId("b1a35f44-abe1-4afc-82eb-372889775b29 new");
    assertEquals("b1a35f44-abe1-4afc-82eb-372889775b29 new", f.getVorlaufId());
  }

  @Test
  void testSetRuecklaufId() {
    f.setRuecklaufId("a20772af-076f-421e-9c64-a78b1ce5017b new");
    assertEquals("a20772af-076f-421e-9c64-a78b1ce5017b new", f.getRuecklaufId());
  }

  @Test
  void testSetAussentemperaturCode() {
    f.setAussentemperaturCode("433_T new");
    assertEquals("433_T new", f.getAussentemperaturCode());
  }

  @Test
  void testSetBrennwertkessel() {
    f.setBrennwertkessel(false);
    assertEquals(false, f.getBrennwertkessel());
  }

  @Test
  void testSetNutzungsgradId() {
    f.setNutzungsgradId("ab7407da-f066-40e6-a975-6288ffa5ddb3 new");
    assertEquals("ab7407da-f066-40e6-a975-6288ffa5ddb3 new", f.getNutzungsgradId());
  }

  @Test
  void testSetAuslastungKgrId() {
    f.setAuslastungKgrId("cd56cb68-f551-4eac-8db2-4ed8d1ac011f new");
    assertEquals("cd56cb68-f551-4eac-8db2-4ed8d1ac011f new", f.getAuslastungKgrId());
  }

  @Test
  void testSetDeltaTemperatureId() {
    f.setDeltaTemperatureId("3db141b3-5414-4dea-b696-51e89c74494e new");
    assertEquals("3db141b3-5414-4dea-b696-51e89c74494e new", f.getDeltaTemperatureId());
  }

  @Test
  void testSetVolumenstromId() {
    f.setVolumenstromId("607f72ce-7172-40f3-a1de-c0305f3b482f new");
    assertEquals("607f72ce-7172-40f3-a1de-c0305f3b482f new", f.getVolumenstromId());
  }

  @Test
  void testSetLeistungId() {
    f.setLeistungId("ee669174-10db-4e40-9b67-6b88a15707ee new");
    assertEquals("ee669174-10db-4e40-9b67-6b88a15707ee new", f.getLeistungId());
  }

  @Test
  void testSetTww() {
    f.setTww(false);
    assertEquals(false, f.getTww());
  }

  @Test
  void testSetUtilizationRatePreviousWeek() {
    f.setUtilizationRatePreviousWeek(80.2);
    assertEquals(80.2, f.getUtilizationRatePreviousWeek());
  }

  @Test
  void testSetTextFragments() {
    f.setTextFragments("Textfragments.. new");
    assertEquals("Textfragments.. new", f.getTextFragments());
  }

  @Test
  void testSetTextFragmentsPrev() {
    f.setTextFragmentsPrev("Textfragments previous.. new");
    assertEquals("Textfragments previous.. new", f.getTextFragmentsPrev());
  }

  @Test
  void testSetEinsparzaehlerobjektid() {
    f.setEinsparzaehlerobjektid("27249 new");
    assertEquals("27249 new", f.getEinsparzaehlerobjektid());
  }

  @Test
  void testSetVersorgungstyp() {
    f.setVersorgungstyp("TWW + Heizung new");
    assertEquals("TWW + Heizung new", f.getVersorgungstyp());
  }

  @Test
  void testSetLiegenschaftObjectId() {
    f.setLiegenschaftObjectId("25355 new");
    assertEquals("25355 new", f.getLiegenschaftObjectId());
  }

  @Test
  void testSetRegelparameterSollWerteObjectId() {
    f.setRegelparameterSollWerteObjectId("30362 new");
    assertEquals("30362 new", f.getRegelparameterSollWerteObjectId());
  }

  @Test
  void testCalcTwwWmzNot1() {
    f.setWmzEneffco(0);
    f.calcTww();
    assertEquals(false, f.getTww());
    f.setWmzEneffco(1);
    f.setVersorgungstyp("Heizung + TWW");
    f.calcTww();
    assertEquals(true, f.getTww());
    f.setVersorgungstyp("Heizung");
    f.calcTww();
    assertEquals(false, f.getTww());
  }

  @Test
  void testCalcTwwTrue() {
    f.setWmzEneffco(1);
    f.setVersorgungstyp("Heizung + TWW");
    f.calcTww();
    assertEquals(true, f.getTww());
    f.setVersorgungstyp("Heizung");
    f.calcTww();
    assertEquals(false, f.getTww());
  }

  @Test
  void testCalcTwwFalse() {
    f.setWmzEneffco(1);
    f.setVersorgungstyp("Heizung");
    f.calcTww();
    assertEquals(false, f.getTww());
  }

  // TODO: test toJson, toString
}
