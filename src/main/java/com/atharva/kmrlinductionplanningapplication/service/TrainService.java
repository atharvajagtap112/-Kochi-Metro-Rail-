package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import com.atharva.kmrlinductionplanningapplication.repository.FitnessCertificateRepository;
import com.atharva.kmrlinductionplanningapplication.repository.JobCardRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;
    private final JobCardRepository jobCardRepository;
    private final FitnessCertificateRepository fitnessCertificateRepository;

    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    public Optional<Train> getTrainById(Long id) {
        return trainRepository.findById(id);
    }

    public Optional<Train> getTrainByNumber(String trainNumber) {
        return trainRepository.findByTrainNumber(trainNumber);
    }

    public List<Train> getAvailableTrainsForInduction() {
        return trainRepository.findAvailableTrainsWithNoOpenJobCards(TrainStatus.ACTIVE);
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

        // Check if train has any open job cards
        boolean hasOpenJobCards = !jobCardRepository.findOpenJobCardsByTrainId(trainId).isEmpty();
        if (hasOpenJobCards) {
            return false;
        }

        // Check if all fitness certificates are valid
        boolean hasValidCertificates = fitnessCertificateRepository
                .hasAllValidCertificates(trainId, LocalDate.now());
        if (!hasValidCertificates) {
            return false;
        }

        // Check if maintenance is due
        if (calculateMaintenanceDue(train)) {
            return false;
        }

        // Check if cleaning is due
        if (calculateCleaningDue(train)) {
            return false;
        }

        return true;
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