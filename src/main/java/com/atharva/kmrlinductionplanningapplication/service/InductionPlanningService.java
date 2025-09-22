package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.Train;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class InductionPlanningService {

    private  TrainService trainService;
    private  JobCardService jobCardService;
    private  FitnessCertificateService certificateService;
    private  CleaningService cleaningService;
    private  BrandingService brandingService;
    private  StablingGeometryService stablingService;

    public InductionPlanningService(TrainService trainService, JobCardService jobCardService, FitnessCertificateService certificateService, CleaningService cleaningService, BrandingService brandingService, StablingGeometryService stablingService) {
        this.trainService = trainService;
        this.jobCardService = jobCardService;
        this.certificateService = certificateService;
        this.cleaningService = cleaningService;
        this.brandingService = brandingService;
        this.stablingService = stablingService;
    }

    public Map<String, Object> generateInductionPlan() {
        Map<String, Object> plan = new HashMap<>();

        // 🔥 FIXED: Now correctly gets List<Train>
        List<Train> allTrains = trainService.getAllTrains();
        List<Train> availableTrains = trainService.getAvailableTrainsForInduction(); // Returns List<Train>
        List<Train> maintenanceTrains = trainService.getTrainsInMaintenance();

        // Check all 6 parameters for each train
        Map<java.lang.Long, Map<String, Object>> trainStatuses = new HashMap<>();

        for (Train train : allTrains) {
            Map<String, Object> status = evaluateTrainReadiness(train);
            trainStatuses.put(train.getTrainId(), status);
        }

        // Generate recommendations
        List<java.lang.Long> recommendedForService = availableTrains.stream()
                .filter(train -> {
                    Map<String, Object> status = trainStatuses.get(train.getTrainId());
                    return (Boolean) status.get("readyForService");
                })
                .map(Train::getTrainId)
                .toList();

        // Schedule cleaning for tonight
        List<String> cleaningScheduled = cleaningService.scheduleDailyCleaning()
                .stream()
                .map(task -> task.getTaskId())
                .toList();

        // Get optimal stabling assignments
        Map<String, Object> stablingPlan = stablingService.getOptimalBayAssignment();

        // Prepare final plan
        plan.put("totalTrains", allTrains.size());
        plan.put("availableForService", recommendedForService.size());
        plan.put("inMaintenance", maintenanceTrains.size());
        plan.put("recommendedTrainsForService", recommendedForService);
        plan.put("trainsInMaintenance", maintenanceTrains.stream().map(Train::getTrainId).toList());
        plan.put("trainStatuses", trainStatuses);
        plan.put("cleaningTasksScheduled", cleaningScheduled);
        plan.put("stablingPlan", stablingPlan);
        plan.put("planGeneratedAt", java.time.LocalDateTime.now());

        return plan;
    }

    private Map<String, Object> evaluateTrainReadiness(Train train) {
        Map<String, Object> status = new HashMap<>();

        // 1. Job Card Status
        boolean hasOpenJobCards = !jobCardService.getJobCardsByTrain(train.getTrainId()).isEmpty();
        status.put("hasOpenJobCards", hasOpenJobCards);

        // 2. Fitness Certificates
        boolean hasValidCertificates = certificateService.hasAllValidCertificates(train.getTrainId());
        status.put("hasValidCertificates", hasValidCertificates);

        // 3. Maintenance Due
        boolean maintenanceDue = trainService.isTrainMaintenanceDue(train.getTrainId());
        status.put("maintenanceDue", maintenanceDue);

        // 4. Cleaning Due
        boolean cleaningDue = trainService.isTrainCleaningDue(train.getTrainId());
        status.put("cleaningDue", cleaningDue);

        // 5. Mileage Balance
        double mileageBalance = trainService.calculateMileageBalance(train.getTrainId());
        status.put("mileageBalance", mileageBalance);

        // 6. Branding Requirements (always allow - doesn't block service)
        Map<String, Object> brandingReport = brandingService.getTrainExposureReport(train.getTrainId());
        status.put("brandingStatus", brandingReport);

        // Final Decision
        boolean readyForService = !hasOpenJobCards && hasValidCertificates && !maintenanceDue && !cleaningDue;
        status.put("readyForService", readyForService);

        // Reason if not ready
        if (!readyForService) {
            String reason = "";
            if (hasOpenJobCards) reason += "Open job cards; ";
            if (!hasValidCertificates) reason += "Invalid certificates; ";
            if (maintenanceDue) reason += "Maintenance due; ";
            if (cleaningDue) reason += "Cleaning due; ";
            status.put("blockingReason", reason);
        }

        return status;
    }
}