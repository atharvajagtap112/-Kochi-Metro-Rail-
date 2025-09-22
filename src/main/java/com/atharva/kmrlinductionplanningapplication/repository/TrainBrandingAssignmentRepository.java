package com.atharva.kmrlinductionplanningapplication.repository;


import com.atharva.kmrlinductionplanningapplication.entity.BrandingContract;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainBrandingAssignmentRepository extends JpaRepository<TrainBrandingAssignment, java.lang.Long> {


    List<TrainBrandingAssignment> findByTrainId(Long trainId);


    List<TrainBrandingAssignment> findByBrandingContract(BrandingContract brandingContract);
}