package com.ewus.ba.analysisService.controller;

import java.util.List;

import javax.validation.Valid;

import com.ewus.ba.analysisService.model.Configuration;
import com.ewus.ba.analysisService.repository.ConfigurationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * REST Controller for Analysis Configurations
 */
@RestController
@RequestMapping("/configurations")
public class ConfigurationController {
    @Autowired
    private ConfigurationRepository configurationRepository;

    @GetMapping
    public List<Configuration> getAllConfigurations() {
        return configurationRepository.findAll();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Configuration> getConfigurationByCode(@PathVariable(value = "code") String code)
            throws Exception {
        // throws ResourceNotFoundException {
        Configuration configuration = configurationRepository.findByCode(code)
                .orElseThrow(() -> new Exception("Configuration not found for this code :: " + code));

        // TODO: .orElseThrow(() -> new ResourceNotFoundException("Configuration not
        // found for
        // this code :: " + code)); (see
        // https://www.javaguides.net/2019/01/spring-boot-microsoft-sql-server-jpa-hibernate-crud-restful-api-tutorial.html)

        return ResponseEntity.ok().body(configuration);
    }

    @PostMapping
    public Configuration createConfiguration(@Valid @RequestBody Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    // TODO: make patch if necessary
    @PutMapping("/{code}")
    public ResponseEntity<Configuration> updateConfiguration(@PathVariable(value = "code") String code,
            @Valid @RequestBody Configuration configurationNew) throws Exception { // ResourceNotFoundException {
        Configuration configuration = configurationRepository.findByCode(code)
                // .orElseThrow(
                // () -> new ResourceNotFoundException("Configuration not found for this id :: "
                // + configurationId));
                .orElseThrow(() -> new Exception("Configuration not found for this code :: " + code));

        configuration.setIsAnalysis1Active(configurationNew.getIsAnalysis1Active());
        configuration.setIsAnalysis2Active(configurationNew.getIsAnalysis2Active());

        final Configuration updatedConfiguration = configurationRepository.save(configuration);
        return ResponseEntity.ok(updatedConfiguration);
    }

}
