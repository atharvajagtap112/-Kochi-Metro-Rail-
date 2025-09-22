package com.atharva.kmrlinductionplanningapplication.service;

import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import com.atharva.kmrlinductionplanningapplication.repository.FitnessCertificateRepository;
import com.atharva.kmrlinductionplanningapplication.repository.JobCardRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainBrandingAssignmentRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class TrainService {
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private JobCardRepository jobCardRepository;
    @Autowired
    private FitnessCertificateRepository fitnessCertificateRepository;
    @Autowired
    private TrainBrandingAssignmentRepository brandingAssignmentRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }

    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }

    public List<Train> saveAllTrains(List<Train> trains) {
        trains.forEach(train -> train.setLastUpdated(LocalDateTime.now()));
        return trainRepository.saveAll(trains);
    }

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

    private boolean isTrainCompletelyReady(Train train) {
        // 1. No open job cards
        boolean hasNoOpenJobCards =train.getJobCards().isEmpty()?true: jobCardRepository.findOpenJobCardsByTrainId(train.getTrainId()).isEmpty();
        System.out.println(train.getTrainId()+":"+"Open Jobcards"+hasNoOpenJobCards);
        // 2. All fitness certificates are valid
        boolean hasAllValidCertificates =train.getFitnessCertificates().isEmpty()?true:  fitnessCertificateRepository
                .hasAllValidCertificates(train.getTrainId(), LocalDate.now());

        // 3. Maintenance not due
        boolean maintenanceNotDue = !calculateMaintenanceDue(train);

        // 4. Cleaning done
        boolean cleaningDone = !calculateCleaningDue(train);


        // Debug logging
        System.out.println("Train " + train.getTrainNumber() + ":");
        System.out.println("  - hasNoOpenJobCards: " + hasNoOpenJobCards);
        System.out.println("  - hasAllValidCertificates: " + hasAllValidCertificates);
        System.out.println("  - maintenanceNotDue: " + maintenanceNotDue);
        System.out.println("  - cleaningDone: " + cleaningDone);
        System.out.println("  - Overall ready: " + (hasNoOpenJobCards && hasAllValidCertificates && maintenanceNotDue && cleaningDone));

        return hasNoOpenJobCards && hasAllValidCertificates && maintenanceNotDue && cleaningDone;
    }

    private Map<String, Object> buildAvailableTrainResponse(Train train) {
        Map<String, Object> response = new HashMap<>();

        response.put("trainId", train.getTrainId());
        response.put("trainNumber", train.getTrainNumber());
        response.put("status", train.getStatus());
        response.put("currentOdometer", train.getCurrentOdometer());
        response.put("depotLocation", train.getDepotLocation());

        response.put("jobCardsStatus", "ALL_CLOSED");
        response.put("certificatesStatus", "ALL_VALID");
        response.put("maintenanceStatus", "NOT_DUE");
        response.put("cleaningStatus", "DONE");

        double mileageBalance = calculateMileageBalance(train.getTrainId());
        response.put("mileageBalance", mileageBalance);
        response.put("mileageUtilization", calculateMileageUtilization(train));

        // ✅ Fixed branding info
        Map<String, Object> brandingInfo = getBrandingInfoForTrain(train.getTrainId());
        response.put("brandingInfo", brandingInfo);

        int priorityScore = calculatePriorityScore(train, brandingInfo);
        response.put("priorityScore", priorityScore);
        response.put("recommendedForService", true);

        response.put("lastUpdated", LocalDateTime.now());

        return response;
    }

    // ✅ Fixed to use trainId
    private Map<String, Object> getBrandingInfoForTrain(Long trainId) {
        Map<String, Object> brandingInfo = new HashMap<>();

        List<TrainBrandingAssignment> assignments = brandingAssignmentRepository.findByTrainId(trainId);

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
                    contract.put("brandingType", assignment.getBrandingContract().getBrandingType());
                    contract.put("requiredHours", assignment.getBrandingContract().getRequiredHours());
                    contract.put("exposedHours", assignment.getTotalHoursExposed());

                    double completion = (double) assignment.getTotalHoursExposed() /
                            assignment.getBrandingContract().getRequiredHours() * 100;
                    contract.put("completionPercentage", completion);
                    contract.put("isAtRisk", completion < 80);

                    return contract;
                })
                .toList();

        brandingInfo.put("activeContracts", activeContracts);

        boolean hasAtRiskContracts = activeContracts.stream()
                .anyMatch(contract -> (Boolean) contract.get("isAtRisk"));

        if (hasAtRiskContracts) {
            brandingInfo.put("priorityLevel", "HIGH");
        } else if (!activeContracts.isEmpty()) {
            brandingInfo.put("priorityLevel", "MEDIUM");
        } else {
            brandingInfo.put("priorityLevel", "LOW");
        }

        brandingInfo.put("atRiskContracts", hasAtRiskContracts);

        return brandingInfo;
    }

    private int calculatePriorityScore(Train train, Map<String, Object> brandingInfo) {
        int score = 100;

        String priorityLevel = (String) brandingInfo.get("priorityLevel");
        switch (priorityLevel) {
            case "HIGH" -> score += 50;
            case "MEDIUM" -> score += 25;
            case "LOW" -> score += 0;
        }

        double mileageBalance = calculateMileageBalance(train.getTrainId());
        if (mileageBalance > 2000) {
            score += 20;
        } else if (mileageBalance > 1000) {
            score += 10;
        } else {
            score -= 10;
        }

        if (train.getLastCleaningDateTime() != null) {
            long hoursSinceCleaning = java.time.Duration.between(
                    train.getLastCleaningDateTime(), LocalDateTime.now()
            ).toHours();

            if (hoursSinceCleaning < 12) {
                score += 10;
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
            return true;
        }

        LocalDateTime cleaningDueTime = train.getLastCleaningDateTime()
                .plusHours(24);

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