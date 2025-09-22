package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.FitnessCertificate;
import com.atharva.kmrlinductionplanningapplication.service.FitnessCertificateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/certificates")

@CrossOrigin(origins = "*")
public class FitnessCertificateController {
    @Autowired
    private  FitnessCertificateService certificateService;

    @GetMapping
    public ResponseEntity<List<FitnessCertificate>> getAllCertificates() {
        List<FitnessCertificate> certificates = certificateService.getAllCertificates();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FitnessCertificate> getCertificateById(@PathVariable Long id) {
        Optional<FitnessCertificate> certificate = certificateService.getCertificateById(id);
        return certificate.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<List<FitnessCertificate>> getCertificatesByTrain(@PathVariable Long trainId) {
        List<FitnessCertificate> certificates = certificateService.getCertificatesByTrainId(trainId);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<FitnessCertificate>> getExpiredCertificates() {
        List<FitnessCertificate> expiredCerts = certificateService.getExpiredCertificates();
        return ResponseEntity.ok(expiredCerts);
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<FitnessCertificate>> getCertificatesExpiringSoon() {
        List<FitnessCertificate> expiringSoon = certificateService.getCertificatesExpiringSoon();
        return ResponseEntity.ok(expiringSoon);
    }

    @GetMapping("/train/{trainId}/valid")
    public ResponseEntity<Boolean> hasAllValidCertificates(@PathVariable Long trainId) {
        boolean hasValid = certificateService.hasAllValidCertificates(trainId);
        return ResponseEntity.ok(hasValid);
    }

    @GetMapping("/train/{trainId}/status")
    public ResponseEntity<String> getTrainCertificationStatus(@PathVariable Long trainId) {
        String status = certificateService.getTrainCertificationStatus(trainId);
        return ResponseEntity.ok(status);
    }

    @PostMapping
    public ResponseEntity<FitnessCertificate> createCertificate(@RequestBody FitnessCertificate certificate) {
        FitnessCertificate savedCertificate = certificateService.createCertificate(certificate);
        return ResponseEntity.ok(savedCertificate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FitnessCertificate> updateCertificate(@PathVariable Long id,
                                                                @RequestBody FitnessCertificate certificate) {
        certificate.setCertificateId(id);
        FitnessCertificate updatedCertificate = certificateService.updateCertificate(certificate);
        return ResponseEntity.ok(updatedCertificate);
    }

    @PutMapping("/{id}/revoke")
    public ResponseEntity<String> revokeCertificate(@PathVariable Long id) {
        boolean success = certificateService.revokeCertificate(id);
        if (success) {
            return ResponseEntity.ok("Certificate revoked successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long id) {
        boolean success = certificateService.deleteCertificate(id);
        if (success) {
            return ResponseEntity.ok("Certificate deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}