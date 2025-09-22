package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.BrandingContract;
import com.atharva.kmrlinductionplanningapplication.entity.BrandingExposureLog;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;
import com.atharva.kmrlinductionplanningapplication.service.BrandingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/branding")

@CrossOrigin(origins = "*")
public class BrandingController {

    @Autowired
    private  BrandingService brandingService;
    public BrandingController (BrandingService brandingService) {
        this.brandingService=brandingService;
    }

    // CONTRACTS
    @GetMapping("/contracts")
    public ResponseEntity<List<BrandingContract>> getAllContracts() {
        List<BrandingContract> contracts = brandingService.getAllContracts();
        return ResponseEntity.ok(contracts);
    }

    @GetMapping("/contracts/{id}")
    public ResponseEntity<BrandingContract> getContractById(@PathVariable Long id) {
        Optional<BrandingContract> contract = brandingService.getContractById(id);
        return contract.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/contracts/active")
    public ResponseEntity<List<BrandingContract>> getActiveContracts() {
        List<BrandingContract> activeContracts = brandingService.getActiveContracts();
        return ResponseEntity.ok(activeContracts);
    }

    @PostMapping("/contracts")
    public ResponseEntity<BrandingContract> createContract(@RequestBody BrandingContract contract) {
        BrandingContract savedContract = brandingService.createContract(contract);
        return ResponseEntity.ok(savedContract);
    }

    @PostMapping("all/contracts")
    public ResponseEntity<List<BrandingContract>> createAllContract(@RequestBody List<BrandingContract> contracts) {
        for (BrandingContract contract : contracts) {
         brandingService.createContract(contract);
        }
        return ResponseEntity.ok(contracts);
    }

    // ASSIGNMENTS
    @GetMapping("/assignments")
    public ResponseEntity<List<TrainBrandingAssignment>> getAllAssignments() {
        List<TrainBrandingAssignment> assignments = brandingService.getAllAssignments();
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/assignments/train/{trainId}")
    public ResponseEntity<List<TrainBrandingAssignment>> getAssignmentsByTrain(@PathVariable Long trainId) {
        List<TrainBrandingAssignment> assignments = brandingService.getAssignmentsByTrain(trainId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/assignments/contract/{contractId}")
    public ResponseEntity<List<TrainBrandingAssignment>> getAssignmentsByContract(@PathVariable Long contractId) {
        List<TrainBrandingAssignment> assignments = brandingService.getAssignmentsByContract(contractId);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/assignments")
    public ResponseEntity<TrainBrandingAssignment> createAssignment(@RequestBody TrainBrandingAssignment assignment) {
        TrainBrandingAssignment savedAssignment = brandingService.createAssignment(assignment);
        return ResponseEntity.ok(savedAssignment);
    }

    @PostMapping("/assignments/assign-train")
    public ResponseEntity<TrainBrandingAssignment> assignBrandingToTrain(@RequestBody Map<String, Long> request) {
        Long trainId = request.get("trainId");
        Long contractId = request.get("contractId");
        TrainBrandingAssignment assignment = brandingService.assignBrandingToTrain(trainId, contractId);
        return ResponseEntity.ok(assignment);
    }

    // EXPOSURE LOGGING
    @PostMapping("/log-exposure")
    public ResponseEntity<BrandingExposureLog> logExposure(@RequestBody BrandingExposureLog exposureLog) {
        BrandingExposureLog savedLog = brandingService.logExposure(exposureLog);
        return ResponseEntity.ok(savedLog);
    }

    @PostMapping("/log-exposure/simple")
    public ResponseEntity<BrandingExposureLog> logExposureSimple(@RequestBody Map<String, Object> request) {
        Long trainId = ((Number) request.get("trainId")).longValue();
        Integer hoursExposed = ((Number) request.get("hoursExposed")).intValue();
        String remarks = (String) request.get("remarks");

        BrandingExposureLog log = brandingService.logExposureForTrain(trainId, hoursExposed, remarks);
        return ResponseEntity.ok(log);
    }

    // REPORTS
    @GetMapping("/exposure-report/contract/{contractId}")
    public ResponseEntity<Map<String, Object>> getContractExposureReport(@PathVariable Long contractId) {
        Map<String, Object> report = brandingService.getContractExposureReport(contractId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/exposure-report/train/{trainId}")
    public ResponseEntity<Map<String, Object>> getTrainExposureReport(@PathVariable Long trainId) {
        Map<String, Object> report = brandingService.getTrainExposureReport(trainId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/contracts/at-risk")
    public ResponseEntity<List<BrandingContract>> getContractsAtRisk() {
        List<BrandingContract> atRiskContracts = brandingService.getContractsAtRisk();
        return ResponseEntity.ok(atRiskContracts);
    }
}