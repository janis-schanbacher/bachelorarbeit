package com.ewus.ba.analysisService.repository;

import com.ewus.ba.analysisService.model.FacilityAnalysisConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IFacilityAnalysisConfigurationRepository
    extends JpaRepository<FacilityAnalysisConfiguration, String> {}
