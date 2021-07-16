package com.ewus.ba.analysisService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ewus.ba.analysisService.model.AnalysisObject;

@Repository
public interface AnalysisObjectRepository extends JpaRepository<AnalysisObject, String> {
}
