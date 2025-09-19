package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import com.atharva.kmrlinductionplanningapplication.repository.FitnessCertificateRepository;
import com.atharva.kmrlinductionplanningapplication.repository.JobCardRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainBrandingAssignmentRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service

public class TrainService {

    private  TrainRepository trainRepository;
    private  JobCardRepository jobCardRepository;
    private  FitnessCertificateRepository fitnessCertificateRepository;
    private  TrainBrandingAssignmentRepository brandingAssignmentRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }

    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }

    // 🔥 UPDATED: Complete availability check with all 6 parameters
    public List<Map<String, Object>> getAvailableTrainsWithDetails() {
        List<Train> allActiveTrains = trainRepository.findByStatus(TrainStatus.ACTIVE);

        return allActiveTrains.stream()
                .filter(this::isTrainCompletelyReady)
                .map(this::buildAvailableTrainResponse)
                .toList();
    }


    public List<Train> getAvailableTrainsForInduction() {
        List<Train> allActiveTrains = trainRepository.findByStatus(TrainStatus.ACTIVE);

        return allActiveTrains.stream()
                .filter(this::isTrainCompletelyReady)
                .toList();
    }

    // 🔥 NEW: Check ALL conditions for train readiness
    private boolean isTrainCompletelyReady(Train train) {
        // 1. No open job cards (all must be CLOSED)
        boolean hasNoOpenJobCards = jobCardRepository.findOpenJobCardsByTrainId(train.getTrainId()).isEmpty();

        // 2. All fitness certificates are valid and not expired
        boolean hasAllValidCertificates = fitnessCertificateRepository
                .hasAllValidCertificates(train.getTrainId(), LocalDate.now());

        // 3. Maintenance not due
        boolean maintenanceNotDue = !calculateMaintenanceDue(train);

        // 4. Cleaning is done (within the cleaning period)
        boolean cleaningDone = !calculateCleaningDue(train);

        // All conditions must be true for train to be available
        return hasNoOpenJobCards && hasAllValidCertificates && maintenanceNotDue && cleaningDone;
    }

    // 🔥 NEW: Build comprehensive response with branding info
    private Map<String, Object> buildAvailableTrainResponse(Train train) {
        Map<String, Object> response = new HashMap<>();

        // Basic train info
        response.put("trainId", train.getTrainId());
        response.put("trainNumber", train.getTrainNumber());
        response.put("status", train.getStatus());
        response.put("currentOdometer", train.getCurrentOdometer());
        response.put("depotLocation", train.getDepotLocation());

        // Readiness status
        response.put("jobCardsStatus", "ALL_CLOSED");
        response.put("certificatesStatus", "ALL_VALID");
        response.put("maintenanceStatus", "NOT_DUE");
        response.put("cleaningStatus", "DONE");

        // Mileage info
        double mileageBalance = calculateMileageBalance(train.getTrainId());
        response.put("mileageBalance", mileageBalance);
        response.put("mileageUtilization", calculateMileageUtilization(train));

        // 🔥 BRANDING INFORMATION - Critical for operations team!
        Map<String, Object> brandingInfo = getBrandingInfoForTrain(train.getTrainId());
        response.put("brandingInfo", brandingInfo);

        // Priority score (higher = should run first)
        int priorityScore = calculatePriorityScore(train, brandingInfo);
        response.put("priorityScore", priorityScore);
        response.put("recommendedForService", true);

        // Last updated
        response.put("lastUpdated", LocalDateTime.now());

        return response;
    }

    // 🔥 NEW: Get complete branding information
    private Map<String, Object> getBrandingInfoForTrain(Long trainId) {
        Map<String, Object> brandingInfo = new HashMap<>();

        List<TrainBrandingAssignment> assignments = brandingAssignmentRepository.findByTrain(
                trainRepository.findById(trainId).orElse(null)
        );

        if (assignments.isEmpty()) {
            brandingInfo.put("hasBranding", false);
            brandingInfo.put("totalContracts", 0);
            brandingInfo.put("priorityLevel", "LOW");
            return brandingInfo;
        }

        brandingInfo.put("hasBranding", true);
        brandingInfo.put("totalContracts", assignments.size());

        // Get active contracts and their details
        List<Map<String, Object>> activeContracts = assignments.stream()
                .filter(assignment -> {
                    LocalDate today = LocalDate.now();
                    return assignment.getBrandingContract().getStartDate().isBefore(today.plusDays(1)) &&
                            assignment.getBrandingContract().getEndDate().isAfter(today.minusDays(1));
                })
                .map(assignment -> {
                    Map<String, Object> contract = new HashMap<>();
                    contract.put("contractId", assignment.getBrandingContract().getContractId());
                    contract.put("advertiserName", assignment.getBrandingContract().getAdvertiserName());
                    contract.put("BrandingType", assignment.getBrandingContract().getBrandingType());
                    contract.put("requiredHours", assignment.getBrandingContract().getRequiredHours());
                    contract.put("exposedHours", assignment.getTotalHoursExposed());

                    // Calculate completion percentage
                    double completion = (double) assignment.getTotalHoursExposed() /
                            assignment.getBrandingContract().getRequiredHours() * 100;
                    contract.put("completionPercentage", completion);
                    contract.put("isAtRisk", completion < 80); // Less than 80% is at risk

                    return contract;
                })
                .toList();

        brandingInfo.put("activeContracts", activeContracts);

        // Determine priority based on contract risk
        boolean hasAtRiskContracts = activeContracts.stream()
                .anyMatch(contract -> (Boolean) contract.get("isAtRisk"));

        if (hasAtRiskContracts) {
            brandingInfo.put("priorityLevel", "HIGH"); // Must run to meet SLA
        } else if (!activeContracts.isEmpty()) {
            brandingInfo.put("priorityLevel", "MEDIUM"); // Has branding but not at risk
        } else {
            brandingInfo.put("priorityLevel", "LOW"); // No active branding
        }

        brandingInfo.put("atRiskContracts", hasAtRiskContracts);

        return brandingInfo;
    }

    // 🔥 NEW: Calculate priority score for train selection
    private int calculatePriorityScore(Train train, Map<String, Object> brandingInfo) {
        int score = 100; // Base score

        // Branding priority (most important for revenue)
        String priorityLevel = (String) brandingInfo.get("priorityLevel");
        switch (priorityLevel) {
            case "HIGH" -> score += 50;  // At-risk contracts - must run!
            case "MEDIUM" -> score += 25; // Has branding contracts
            case "LOW" -> score += 0;     // No branding
        }

        // Mileage balance (prevent maintenance due soon)
        double mileageBalance = calculateMileageBalance(train.getTrainId());
        if (mileageBalance > 2000) {
            score += 20; // Plenty of mileage left
        } else if (mileageBalance > 1000) {
            score += 10; // Moderate mileage left
        } else {
            score -= 10; // Low mileage, should go to maintenance soon
        }

        // Recent cleaning (fresher trains get slight priority)
        if (train.getLastCleaningDateTime() != null) {
            long hoursSinceCleaning = java.time.Duration.between(
                    train.getLastCleaningDateTime(), LocalDateTime.now()
            ).toHours();

            if (hoursSinceCleaning < 12) {
                score += 10; // Recently cleaned
            }
        }

        return score;
    }

    private double calculateMileageUtilization(Train train) {
        if (train.getCurrentOdometer() == null || train.getOdometerAtLastMaintenance() == null) {
            return 0.0;
        }

        double mileageSinceLastMaintenance = train.getCurrentOdometer() - train.getOdometerAtLastMaintenance();
        return (mileageSinceLastMaintenance / train.getMaintenanceInterval()) * 100;
    }

    public List<Train> getTrainsInMaintenance() {
        return trainRepository.findTrainsWithOpenJobCards();
    }

    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }

    public boolean isTrainMaintenanceDue(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isPresent()) {
            Train train = trainOpt.get();
            return calculateMaintenanceDue(train);
        }
        return false;
    }

    public boolean isTrainCleaningDue(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isPresent()) {
            Train train = trainOpt.get();
            return calculateCleaningDue(train);
        }
        return false;
    }

    // 🔥 UPDATED: Complete validation with all parameters
    public boolean validateTrainForService(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isEmpty()) {
            return false;
        }

        Train train = trainOpt.get();
        return isTrainCompletelyReady(train);
    }

    private boolean calculateMaintenanceDue(Train train) {
        if (train.getCurrentOdometer() == null || train.getOdometerAtLastMaintenance() == null) {
            return false;
        }

        double mileageSinceLastMaintenance = train.getCurrentOdometer() - train.getOdometerAtLastMaintenance();
        return mileageSinceLastMaintenance >= train.getMaintenanceInterval();
    }

    private boolean calculateCleaningDue(Train train) {
        if (train.getLastCleaningDateTime() == null) {
            return true; // No cleaning record, so it's due
        }

        LocalDateTime cleaningDueTime = train.getLastCleaningDateTime()
                .plusHours(train.getCleaningPeriod());

        return LocalDateTime.now().isAfter(cleaningDueTime);
    }

    public double calculateMileageBalance(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isEmpty()) {
            return 0.0;
        }

        Train train = trainOpt.get();
        if (train.getCurrentOdometer() == null || train.getOdometerAtLastMaintenance() == null) {
            return 0.0;
        }

        double remainingMileage = train.getMaintenanceInterval() -
                (train.getCurrentOdometer() - train.getOdometerAtLastMaintenance());

        return Math.max(0, remainingMileage);
    }
}