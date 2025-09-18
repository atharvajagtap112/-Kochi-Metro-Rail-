package com.atharva.kmrlinductionplanningapplication.repository;


import com.atharva.kmrlinductionplanningapplication.entity.FitnessCertificate;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.CertificateStatus;
import com.atharva.kmrlinductionplanningapplication.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FitnessCertificateRepository extends JpaRepository<FitnessCertificate, Long> {

    List<FitnessCertificate> findByTrain(Train train);

    List<FitnessCertificate> findByStatus(CertificateStatus status);

    List<FitnessCertificate> findByDepartment(Department department);

    List<FitnessCertificate> findByTrainAndDepartment(Train train, Department department);

    @Query("SELECT fc FROM FitnessCertificate fc WHERE fc.expiryDate < :currentDate")
    List<FitnessCertificate> findExpiredCertificates(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT fc FROM FitnessCertificate fc WHERE fc.expiryDate BETWEEN :currentDate AND :warningDate")
    List<FitnessCertificate> findCertificatesExpiringWithinDays(@Param("currentDate") LocalDate currentDate,
                                                                @Param("warningDate") LocalDate warningDate);

    @Query("SELECT fc FROM FitnessCertificate fc WHERE fc.train.trainId = :trainId AND fc.status = 'VALID'")
    List<FitnessCertificate> findValidCertificatesByTrainId(@Param("trainId") Long trainId);

    @Query("SELECT CASE WHEN COUNT(fc) = 3 THEN true ELSE false END FROM FitnessCertificate fc " +
            "WHERE fc.train.trainId = :trainId AND fc.status = 'VALID' AND fc.expiryDate >= :currentDate")
    boolean hasAllValidCertificates(@Param("trainId") Long trainId, @Param("currentDate") LocalDate currentDate);
}