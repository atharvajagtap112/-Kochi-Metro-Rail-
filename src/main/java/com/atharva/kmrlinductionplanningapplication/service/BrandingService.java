package com.atharva.kmrlinductionplanningapplication.service;

import com.atharva.kmrlinductionplanningapplication.entity.BrandingContract;
import com.atharva.kmrlinductionplanningapplication.entity.BrandingExposureLog;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;
import com.atharva.kmrlinductionplanningapplication.repository.BrandingContractRepository;
import com.atharva.kmrlinductionplanningapplication.repository.BrandingExposureLogRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainBrandingAssignmentRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BrandingService {

    private final BrandingContractRepository contractRepository;
    private final TrainBrandingAssignmentRepository assignmentRepository;
    private final BrandingExposureLogRepository exposureLogRepository;
    private final TrainRepository trainRepository;

    public BrandingService(BrandingContractRepository contractRepository,
                           TrainBrandingAssignmentRepository assignmentRepository,
                           BrandingExposureLogRepository exposureLogRepository,
                           TrainRepository trainRepository) {
        this.contractRepository = contractRepository;
        this.assignmentRepository = assignmentRepository;
        this.exposureLogRepository = exposureLogRepository;
        this.trainRepository = trainRepository;
    }

    // CONTRACT METHODS
    public List<BrandingContract> getAllContracts() {
        return contractRepository.findAll();
    }

    public Optional<BrandingContract> getContractById(Long id) {
        return contractRepository.findById(id);
    }

    public List<BrandingContract> getActiveContracts() {
        LocalDate today = LocalDate.now();
        return contractRepository.findActiveContracts(today);
    }

    public BrandingContract createContract(BrandingContract contract) {
        return contractRepository.save(contract);
    }

    // ASSIGNMENT METHODS
    public List<TrainBrandingAssignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    // ✅ Fixed to use trainId
    public List<TrainBrandingAssignment> getAssignmentsByTrain(Long trainId) {
        return assignmentRepository.findByTrainId(trainId);
    }

    public List<TrainBrandingAssignment> getAssignmentsByContract(Long contractId) {
        Optional<BrandingContract> contract = contractRepository.findById(contractId);
        if (contract.isPresent()) {
            return assignmentRepository.findByBrandingContract(contract.get());
        }
        return List.of();
    }

    public TrainBrandingAssignment createAssignment(TrainBrandingAssignment assignment) {
        assignment.setAssignmentDate(LocalDate.now());
        return assignmentRepository.save(assignment);
    }

    // ✅ Fixed to use trainId
    public TrainBrandingAssignment assignBrandingToTrain(Long trainId, Long contractId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        Optional<BrandingContract> contractOpt = contractRepository.findById(contractId);

        if (trainOpt.isEmpty() || contractOpt.isEmpty()) {
            throw new RuntimeException("Train or Contract not found");
        }

        TrainBrandingAssignment assignment = new TrainBrandingAssignment();
        assignment.setTrainId(trainId); // ✅ Now using trainId
        assignment.setBrandingContract(contractOpt.get());
        assignment.setAssignmentDate(LocalDate.now());
        assignment.setTotalHoursExposed(0);

        return assignmentRepository.save(assignment);
    }

    // EXPOSURE LOGGING
    public BrandingExposureLog logExposure(BrandingExposureLog exposureLog) {
        exposureLog.setLogDate(LocalDate.now());
        BrandingExposureLog savedLog = exposureLogRepository.save(exposureLog);

        // Update total hours in assignment
        updateAssignmentTotalHours(exposureLog.getTrainBrandingAssignment());

        return savedLog;
    }

    public BrandingExposureLog logExposureForTrain(Long trainId, Integer hoursExposed, String remarks) {
        // ✅ Fixed to use trainId
        List<TrainBrandingAssignment> assignments = getAssignmentsByTrain(trainId);

        if (assignments.isEmpty()) {
            throw new RuntimeException("No branding assignment found for train ID: " + trainId);
        }

        // Use the first active assignment
        TrainBrandingAssignment assignment = assignments.get(0);

        BrandingExposureLog log = new BrandingExposureLog();
        log.setTrainBrandingAssignment(assignment);
        log.setLogDate(LocalDate.now());
        log.setHoursExposed(hoursExposed);
        log.setRemarks(remarks);

        return logExposure(log);
    }

    private void updateAssignmentTotalHours(TrainBrandingAssignment assignment) {
        List<BrandingExposureLog> logs = exposureLogRepository.findByTrainBrandingAssignment(assignment);
        int totalHours = logs.stream().mapToInt(BrandingExposureLog::getHoursExposed).sum();

        assignment.setTotalHoursExposed(totalHours);
        assignmentRepository.save(assignment);
    }

    // REPORTS
    public Map<String, Object> getContractExposureReport(Long contractId) {
        Optional<BrandingContract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isEmpty()) {
            throw new RuntimeException("Contract not found");
        }

        BrandingContract contract = contractOpt.get();
        List<TrainBrandingAssignment> assignments = getAssignmentsByContract(contractId);

        int totalHoursExposed = assignments.stream()
                .mapToInt(TrainBrandingAssignment::getTotalHoursExposed)
                .sum();

        int requiredHours = contract.getRequiredHours();
        double completionPercentage = (double) totalHoursExposed / requiredHours * 100;
        int remainingHours = Math.max(0, requiredHours - totalHoursExposed);

        Map<String, Object> report = new HashMap<>();
        report.put("contractId", contractId);
        report.put("advertiserName", contract.getAdvertiserName());
        report.put("requiredHours", requiredHours);
        report.put("totalHoursExposed", totalHoursExposed);
        report.put("remainingHours", remainingHours);
        report.put("completionPercentage", completionPercentage);
        report.put("isAtRisk", completionPercentage < 80);
        report.put("assignments", assignments);

        return report;
    }

    // ✅ Fixed to use trainId
    public Map<String, Object> getTrainExposureReport(Long trainId) {
        List<TrainBrandingAssignment> assignments = getAssignmentsByTrain(trainId);

        Map<String, Object> report = new HashMap<>();
        report.put("trainId", trainId);
        report.put("assignments", assignments);
        report.put("totalContracts", assignments.size());

        int totalHoursAllContracts = assignments.stream()
                .mapToInt(TrainBrandingAssignment::getTotalHoursExposed)
                .sum();

        report.put("totalHoursExposed", totalHoursAllContracts);

        return report;
    }

    public List<BrandingContract> getContractsAtRisk() {
        LocalDate today = LocalDate.now();
        List<BrandingContract> activeContracts = contractRepository.findActiveContracts(today);

        return activeContracts.stream()
                .filter(this::isContractAtRisk)
                .toList();
    }

    private boolean isContractAtRisk(BrandingContract contract) {
        List<TrainBrandingAssignment> assignments = getAssignmentsByContract(contract.getContractId());

        int totalHoursExposed = assignments.stream()
                .mapToInt(TrainBrandingAssignment::getTotalHoursExposed)
                .sum();

        double completionPercentage = (double) totalHoursExposed / contract.getRequiredHours() * 100;

        return completionPercentage < 80; // 80% threshold for "at risk"
    }
}