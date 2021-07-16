package com.ewus.ba.analysisService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.ewus.ba.analysisService.model.Configuration;

@Repository
public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
    Optional<Configuration> findByCode(String code);
}
