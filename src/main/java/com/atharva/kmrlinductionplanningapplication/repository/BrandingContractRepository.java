package com.atharva.kmrlinductionplanningapplication.repository;


import com.atharva.kmrlinductionplanningapplication.entity.BrandingContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BrandingContractRepository extends JpaRepository<BrandingContract, Long> {

    @Query("SELECT bc FROM BrandingContract bc WHERE bc.startDate <= :currentDate AND bc.endDate >= :currentDate")
    List<BrandingContract> findActiveContracts(@Param("currentDate") LocalDate currentDate);

    List<BrandingContract> findByAdvertiserName(String advertiserName);
}