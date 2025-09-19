package com.atharva.kmrlinductionplanningapplication.repository;


import com.atharva.kmrlinductionplanningapplication.entity.StablingGeometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StablingGeometryRepository extends JpaRepository<StablingGeometry, String> {

    List<StablingGeometry> findByStatus(StablingGeometry.BayStatus status);

    List<StablingGeometry> findByTrackId(String trackId);

    List<StablingGeometry> findByCurrentTrainId(Long trainId);
}