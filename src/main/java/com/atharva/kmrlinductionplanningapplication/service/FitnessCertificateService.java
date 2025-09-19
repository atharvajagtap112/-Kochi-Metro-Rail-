package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.FitnessCertificate;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.CertificateStatus;
import com.atharva.kmrlinductionplanningapplication.repository.FitnessCertificateRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service

public class FitnessCertificateService {

    private  FitnessCertificateRepository certificateRepository;
    private  TrainRepository trainRepository;

    public List<FitnessCertificate> getAllCertificates() {
        return certificateRepository.findAll();
    }

    public Optional<FitnessCertificate> getCertificateById(Long id) {
        return certificateRepository.findById(id);
    }

    public List<FitnessCertificate> getCertificatesByTrain(Long trainId) {
        Optional<Train> train = trainRepository.findById(trainId);
        if (train.isPresent()) {
            return certificateRepository.findByTrain(train.get());
        }
        return List.of();
    }

    public List<FitnessCertificate> getExpiredCertificates() {
        return certificateRepository.findExpiredCertificates(LocalDate.now());
    }

    public List<FitnessCertificate> getCertificatesExpiringSoon() {
        LocalDate currentDate = LocalDate.now();
        LocalDate warningDate = currentDate.plusDays(7); // 7 days warning
        return certificateRepository.findCertificatesExpiringWithinDays(currentDate, warningDate);
    }

    public boolean hasAllValidCertificates(Long trainId) {
        return certificateRepository.hasAllValidCertificates(trainId, LocalDate.now());
    }

    public String getTrainCertificationStatus(Long trainId) {
        List<FitnessCertificate> validCerts = certificateRepository.findValidCertificatesByTrainId(trainId);

        if (validCerts.size() == 3) {
            return "FULLY_CERTIFIED";
        } else if (validCerts.size() > 0) {
            return "PARTIALLY_CERTIFIED";
        } else {
            return "NOT_CERTIFIED";
        }
    }

    public FitnessCertificate createCertificate(FitnessCertificate certificate) {
        return certificateRepository.save(certificate);
    }

    public FitnessCertificate updateCertificate(FitnessCertificate certificate) {
        return certificateRepository.save(certificate);
    }

    public boolean revokeCertificate(Long certificateId) {
        Optional<FitnessCertificate> certOpt = certificateRepository.findById(certificateId);
        if (certOpt.isPresent()) {
            FitnessCertificate certificate = certOpt.get();
            certificate.setStatus(CertificateStatus.REVOKED);
            certificateRepository.save(certificate);
            return true;
        }
        return false;
    }

    public boolean deleteCertificate(Long certificateId) {
        if (certificateRepository.existsById(certificateId)) {
            certificateRepository.deleteById(certificateId);
            return true;
        }
        return false;
    }

    // Auto-update expired certificates
    public void updateExpiredCertificates() {
        List<FitnessCertificate> expiredCerts = certificateRepository.findExpiredCertificates(LocalDate.now());
        for (FitnessCertificate cert : expiredCerts) {
            if (cert.getStatus() == CertificateStatus.VALID) {
                cert.setStatus(CertificateStatus.EXPIRED);
                certificateRepository.save(cert);
            }
        }
    }
}