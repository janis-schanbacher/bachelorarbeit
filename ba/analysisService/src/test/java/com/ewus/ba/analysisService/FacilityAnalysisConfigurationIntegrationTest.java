package com.ewus.ba.analysisService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ewus.ba.analysisService.model.FacilityAnalysisConfiguration;
import com.ewus.ba.analysisService.repository.IFacilityAnalysisConfigurationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FacilityAnalysisConfigurationIntegrationTest {

    private final ObjectMapper mapper = new ObjectMapper();
      @Autowired
      protected MockMvc mockMvc;

    @Autowired
    private IFacilityAnalysisConfigurationRepository facilityAnalysisConfigurationRepository;

    @Test
    public void given_getIndex_then_returnConfigsAnd200() throws Exception {
      ResultActions resultActions = mockMvc.perform(get("/configs"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(9)))
            .andExpect(jsonPath("$[0].id").value("tst.001"))
            .andExpect(jsonPath("$[0].facilitySize").value(true))
            .andExpect(jsonPath("$[0].utilizationRate").value(true))
            .andExpect(jsonPath("$[0].deltaTemperature").value(true))
            .andExpect(jsonPath("$[0].returnTemperature").value(true));

            // Alternatively assert againstFacilityAnalysisConfiguration
            MvcResult result = resultActions.andReturn();
            String contentAsString = result.getResponse().getContentAsString();
            // FacilityAnalysisConfiguration response = mapper.readValue(contentAsString,
            //     FacilityAnalysisConfiguration.class); // if body is a JSONObject
            List<FacilityAnalysisConfiguration> actual = mapper.readValue(contentAsString, new TypeReference<List<FacilityAnalysisConfiguration>>() {});
            assertEquals("tst.001", actual.get(0).getId());
    }

    @Test
    void given_getWithExistingId_then_returnConfigAnd200() throws Exception {
        mockMvc.perform(get("/configs/tst.001"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value("tst.001"))
            .andExpect(jsonPath("$.facilitySize").value(true))
            .andExpect(jsonPath("$.utilizationRate").value(true))
            .andExpect(jsonPath("$.deltaTemperature").value(true))
            .andExpect(jsonPath("$.returnTemperature").value(true));
    }

    @Test
    void given_getWithNotExistingId_then_status404() throws Exception {
        mockMvc.perform(get("/configs/tst.999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void given_postWithoutId_then_badRequest() throws Exception {
        FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(null, true, true, true, true);
        mockMvc.perform(post("/configs")
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void given_postWithEmptyTitle_then_badRequest() throws Exception {
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration("", true, true, true, true);
      mockMvc.perform(post("/configs")
          .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
          .content(mapper.writeValueAsString(config)))
          .andExpect(status().isBadRequest());
    }

    @Test
    void given_postWithInvalidId_then_badRequest() throws Exception {
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration("wrongFormat", true, true, true, true);
      mockMvc.perform(post("/configs")
          .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
          .content(mapper.writeValueAsString(config)))
          .andExpect(status().isBadRequest());
    }

    @Test
    void given_post_then_saveConfigAndReturnLocationAnd201() throws Exception {
      String id = "tst.010";
        FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(id, true, true, true, true);
        mockMvc.perform(post("/configs")
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(header().string("Location", "/configs/" + id))
            .andExpect(status().isCreated());
        assertTrue(facilityAnalysisConfigurationRepository.findById(id).isPresent());
        // cleanup
        facilityAnalysisConfigurationRepository.deleteById(id);
      }

    @Test
    void given_putWithNotExistingConfigId_then_notFound() throws Exception {
      String id = "tst.999";
        FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(id, true, true, true, true);
        mockMvc.perform(put("/configs/" + id)
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(status().isNotFound());
    }

    @Test
    void given_putIdOfUrlNotEqualToIdField_then_badRequest() throws Exception {
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration("tst.001", true, true, true, true);
        mockMvc.perform(put("/configs/tst.002")
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void given_putWithoutId_then_badRequest() throws Exception {
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(null, true, true, true, true);
        mockMvc.perform(put("/configs/tst.001")
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void given_putWithoutTitle_then_badRequest() throws Exception {
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(null, true, true, true, true);
        mockMvc.perform(put("/configs/tst.001")
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void given_putWithEmptyTitle_then_badRequest() throws Exception {
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration("", true, true, true, true);
      mockMvc.perform(
          put("/configs/tst.001").contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
              .content(mapper.writeValueAsString(config)))
          .andExpect(status().isBadRequest());
    }

    @Test
    void given_putWithInvalidId_then_badRequest() throws Exception {
      String id = "wrongFormat";
      FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(id, true, true, true, true);
      mockMvc.perform(
          put("/configs/" + id).contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
          .andExpect(status().isBadRequest());
    }

    @Test
    void given_put_then_updateConfigAnd204() throws Exception {
      String id = "tst.001";
      FacilityAnalysisConfiguration configBkp = facilityAnalysisConfigurationRepository.findById(id).get();
        FacilityAnalysisConfiguration config = new FacilityAnalysisConfiguration(id, true, true, true, false);
        mockMvc.perform(put("/configs/" + id)
            .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
            .content(mapper.writeValueAsString(config)))
            .andExpect(status().isNoContent());
        Optional<FacilityAnalysisConfiguration> savedConfig = facilityAnalysisConfigurationRepository.findById(id);
        assertTrue(savedConfig.isPresent());
        assertEquals(savedConfig.get(), config);
        // cleanup
        facilityAnalysisConfigurationRepository.save(configBkp);
    }

    @Test
    void given_deleteWithNotExistingId_then_notFound() throws Exception {
        mockMvc.perform(delete("/configs/tst.999"))
            .andExpect(status().isNotFound());
    }

    @Test
    void given_delete_then_deleteConfigAnd204() throws Exception {
      String id = "tst.001";
      FacilityAnalysisConfiguration configBkp = facilityAnalysisConfigurationRepository.findById(id).get();
        mockMvc.perform(delete("/configs/" + id))
            .andExpect(status().isNoContent());
        assertTrue(facilityAnalysisConfigurationRepository.findById(id).isEmpty());
        // cleanup
        facilityAnalysisConfigurationRepository.save(configBkp);
    }
}
