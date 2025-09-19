package com.atharva.kmrlinductionplanningapplication.repository;


import com.atharva.kmrlinductionplanningapplication.entity.BrandingContract;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainBrandingAssignmentRepository extends JpaRepository<TrainBrandingAssignment, Long> {

    List<TrainBrandingAssignment> findByTrain(Train train);

    List<TrainBrandingAssignment> findByBrandingContract(BrandingContract brandingContract);
}