package com.atharva.kmrlinductionplanningapplication.repository;


import com.atharva.kmrlinductionplanningapplication.entity.BrandingExposureLog;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandingExposureLogRepository extends JpaRepository<BrandingExposureLog, Long> {

    List<BrandingExposureLog> findByTrainBrandingAssignment(TrainBrandingAssignment assignment);
}