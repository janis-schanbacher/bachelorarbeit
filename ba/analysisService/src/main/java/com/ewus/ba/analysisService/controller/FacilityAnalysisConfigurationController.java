package com.ewus.ba.analysisService.controller;

import com.ewus.ba.analysisService.model.FacilityAnalysisConfiguration;
import com.ewus.ba.analysisService.repository.IFacilityAnalysisConfigurationRepository;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/configs")
@CrossOrigin
public class FacilityAnalysisConfigurationController {

  @Autowired
  private IFacilityAnalysisConfigurationRepository facilityAnalysisConfigurationRepository;

  /**
   * Returns a ResponseEntity with status 200 and all configurations
   *
   * @return a ResponseEntity with status 200 and all configurations
   */
  @GetMapping("")
  public ResponseEntity<List<FacilityAnalysisConfiguration>>
      getAllFacilityAnalysisConfigurations() {
    return ResponseEntity.ok(facilityAnalysisConfigurationRepository.findAll());
  }

  /**
   * Returns a ResponseEntity with status 200 and the facilityAnalysisConfiguration specified by id,
   * or 404 no facilityAnalysisConfiguration with the provided id exists.
   *
   * @param id id of the facilityAnalysisConfiguration to be returned (equals code of the facility)
   * @return a ResponseEntity with status 200 and the facilityAnalysisConfiguration specified by id,
   *     or 404 no facilityAnalysisConfiguration with the provided id exists.
   */
  @GetMapping("/{id}")
  public ResponseEntity<FacilityAnalysisConfiguration> getFacilityAnalysisConfiguration(
      @PathVariable(value = "id") String id) {
    Optional<FacilityAnalysisConfiguration> facilityAnalysisConfiguration =
        facilityAnalysisConfigurationRepository.findById(id);
    return facilityAnalysisConfiguration
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

  /**
   * Returns a ResponseEntity with status 200 and the a list of facilityAnalysisConfigurations for
   * the facilities that are specified by the codes in the codes parameter and have a configuration.
   *
   * @param id id of the facilityAnalysisConfiguration to be returned (equals code of the facility)
   * @return a ResponseEntity with status 200 and the a list of facilityAnalysisConfigurations for
   *     the facilities that are specified by the codes in the codes parameter and have a
   *     configuration.
   */
  @GetMapping("/get-list")
  @ResponseBody
  public List<FacilityAnalysisConfiguration> getFacilityAnalysisConfigurations(
      @RequestParam String codes) {
    // Remove quotation marks and square brackets, transform to lower case, and save results in List
    List<String> codesList =
        Arrays.asList(codes.replaceAll("[\\[\\]\\s\"]*", "").split(",")).stream()
            .map(String::toUpperCase)
            .collect(Collectors.toList());

    List<FacilityAnalysisConfiguration> facilityAnalysisConfigurations =
        facilityAnalysisConfigurationRepository.findAllById(codesList);

    return facilityAnalysisConfigurations;
  }

  /**
   * Saves the provided facilityAnalysisConfiguration
   *
   * @param config FacilityAnalysisConfiguration to be saved
   * @return a ResponseEntity with status 200 and location header, or 400, if analysisIds missing
   */
  @PostMapping("")
  public ResponseEntity<String> createFacilityAnalysisConfiguration(
      @RequestBody FacilityAnalysisConfiguration config) {
    if (config.getId() == null) {
      return ResponseEntity.badRequest().body("Id field is required");
    }
    config.setId(config.getId().toUpperCase().strip());
    if (!config.getId().matches("[a-zA-Z]{3}\\.[0-9]{3}")) {
      return ResponseEntity.badRequest()
          .body("Id field has to be the code of a facility. Format: ABC.123");
    }
    try {
      config = facilityAnalysisConfigurationRepository.save(config);
      return ResponseEntity.created(new URI("/configs/" + config.getId())).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Replaces the facilityAnalysisConfiguration specified by id with the provided
   * facilityAnalysisConfiguration
   *
   * @param config FacilityAnalysisConfiguration to replace the old FacilityAnalysisConfiguration
   * @param id Identifier of the facilityAnalysisConfiguration to be updated (equeals code of
   *     facility)
   * @return a ResponseEntity with status 204 and location header, or 400/404 and error message if
   *     not successful
   */
  @PutMapping("/{id}")
  public ResponseEntity<String> updateFacilityAnalysisConfiguration(
      @RequestBody FacilityAnalysisConfiguration config, @PathVariable(value = "id") String id) {
    if (config.getId() == null) {
      return ResponseEntity.badRequest().body("id field is required");
    }
    config.setId(config.getId().toUpperCase().strip());
    id = id.toUpperCase().strip();
    if (!config.getId().equals(id)) {
      return ResponseEntity.badRequest().body("id field has to be equal to id specified in url");
    }
    if (!config.getId().matches("[a-zA-Z]{3}\\.[0-9]{3}")) {
      return ResponseEntity.badRequest()
          .body("Id field has to be the code of a facility. Format: ABC.123");
    }
    if (facilityAnalysisConfigurationRepository.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }

    try {
      config = facilityAnalysisConfigurationRepository.save(config);
      HttpHeaders headers = new HttpHeaders();
      headers.add(HttpHeaders.LOCATION, "/configs/" + config.getId());
      return ResponseEntity.noContent().headers(headers).build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Deletes the facilityAnalysisConfiguration specified by id
   *
   * @param id Id of the facilityAnalysisConfiguration to be saved (equals code of the facility)
   * @return a ResponseEntity with status 204, or 400/404 if not successful
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteFacilityAnalysisConfiguration(
      @PathVariable(value = "id") String id) {
    id = id.toUpperCase().strip();
    if (facilityAnalysisConfigurationRepository.findById(id).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    try {
      facilityAnalysisConfigurationRepository.deleteById(id);
      return ResponseEntity.noContent().build();
    } catch (Exception e) {
      return ResponseEntity.badRequest().build();
    }
  }
}
