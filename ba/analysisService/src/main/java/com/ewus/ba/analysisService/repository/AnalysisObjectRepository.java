package com.ewus.ba.analysisService.repository;

import com.ewus.ba.analysisService.model.AnalysisObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnalysisObjectRepository extends JpaRepository<AnalysisObject, String> {}
