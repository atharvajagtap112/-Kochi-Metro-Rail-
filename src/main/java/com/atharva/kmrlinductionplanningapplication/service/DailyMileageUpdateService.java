package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.BrandingExposureLog;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.entity.TrainBrandingAssignment;
import com.atharva.kmrlinductionplanningapplication.entity.TripHistory;
import com.atharva.kmrlinductionplanningapplication.repository.BrandingExposureLogRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainBrandingAssignmentRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TripHistoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class DailyMileageUpdateService {

    @Autowired
    private  TrainRepository trainRepository;
    @Autowired
    private  TripHistoryRepository tripHistoryRepository;
    @Autowired
    private  TrainBrandingAssignmentRepository brandingAssignmentRepository;
    @Autowired
    private  BrandingExposureLogRepository exposureLogRepository;

    @Transactional
    public void updateDailyMileageForAllTrains() {
        List<Train> allTrains = trainRepository.findAll();

        for (Train train : allTrains) {
            updateTrainDailyMileage(train.getTrainId());
        }
    }

    @Transactional
    public Map<String, Object> updateTrainDailyMileage(Long trainId) {
        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found: " + trainId);
        }

        Train train = trainOpt.get();
        LocalDate today = LocalDate.now();

        // Get today's trips for this train
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        List<TripHistory> todaysTrips = tripHistoryRepository.findTripsBetween(startOfDay, endOfDay)
                .stream()
                .filter(trip -> trip.getTrain().getTrainId().equals(trainId))
                .toList();

        if (todaysTrips.isEmpty()) {
            return Map.of(
                    "trainId", trainId,
                    "date", today,
                    "mileageAdded", 0.0,
                    "brandingUpdated", false,
                    "message", "No trips found for today"
            );
        }

        // Calculate total mileage for today
        double todaysMileage = todaysTrips.stream()
                .mapToDouble(TripHistory::getTripMileage)
                .sum();

        // Calculate total service hours for today
        int totalServiceMinutes = todaysTrips.stream()
                .mapToInt(TripHistory::getTripDuration)
                .sum();

        int totalServiceHours = totalServiceMinutes / 60;

        // Update train odometer
        double currentOdometer = train.getCurrentOdometer() != null ? train.getCurrentOdometer() : 0.0;
        train.setCurrentOdometer(currentOdometer + todaysMileage);
        trainRepository.save(train);

        // Update branding exposure for all active assignments
        boolean brandingUpdated = updateBrandingExposure(train, todaysMileage, totalServiceHours, todaysTrips);

        return Map.of(
                "trainId", trainId,
                "trainNumber", train.getTrainNumber(),
                "date", today,
                "mileageAdded", todaysMileage,
                "totalTrips", todaysTrips.size(),
                "serviceHours", totalServiceHours,
                "newOdometer", train.getCurrentOdometer(),
                "brandingUpdated", brandingUpdated
        );
    }

    private boolean updateBrandingExposure(Train train, double todaysMileage, int serviceHours, List<TripHistory> trips) {
        List<TrainBrandingAssignment> activeAssignments = brandingAssignmentRepository
                .findByTrain(train)
                .stream()
                .filter(assignment -> assignment.getStatus() == TrainBrandingAssignment.AssignmentStatus.ACTIVE)
                .toList();

        if (activeAssignments.isEmpty()) {
            return false;
        }

        LocalDate today = LocalDate.now();

        for (TrainBrandingAssignment assignment : activeAssignments) {
            // Create exposure log for today
            BrandingExposureLog exposureLog = new BrandingExposureLog();
            exposureLog.setTrainBrandingAssignment(assignment);
            exposureLog.setLogDate(today);
            exposureLog.setHoursExposed(serviceHours);
            exposureLog.setMileageCovered(todaysMileage);

            // Set odometer readings
            if (!trips.isEmpty()) {
                exposureLog.setStartOdometer(trips.get(0).getTrain().getCurrentOdometer() - todaysMileage);
                exposureLog.setEndOdometer(trips.get(trips.size() - 1).getTrain().getCurrentOdometer());
            }

            // Calculate route details
            String routesCovered = trips.stream()
                    .map(TripHistory::getRouteId)
                    .distinct()
                    .reduce((r1, r2) -> r1 + ", " + r2)
                    .orElse("No routes");

            exposureLog.setRoutesCovered(routesCovered);
            exposureLog.setTotalTrips(trips.size());

            // Calculate passenger exposure
            int totalPassengers = trips.stream()
                    .mapToInt(trip -> (int) (trip.getPassengerLoadFactor() * 100)) // Assuming load factor is 0-1
                    .sum();

            exposureLog.setPassengerCount(totalPassengers);
            exposureLog.setServiceType(BrandingExposureLog.ServiceType.REGULAR);
            exposureLog.setExposureQuality(BrandingExposureLog.ExposureQuality.NORMAL);
            exposureLog.setWeatherCondition(BrandingExposureLog.WeatherCondition.CLEAR); // Default, can be updated
            exposureLog.setRemarks("Auto-generated daily exposure log");
            exposureLog.setLoggedBy("SYSTEM");

            // Save exposure log
            exposureLogRepository.save(exposureLog);

            // Update assignment totals
            assignment.setTotalHoursExposed(assignment.getTotalHoursExposed() + serviceHours);
            assignment.setTotalMileageExposed(assignment.getTotalMileageExposed() + todaysMileage);
            assignment.setAverageDailyMileage(
                    (assignment.getTotalMileageExposed() /
                            java.time.temporal.ChronoUnit.DAYS.between(assignment.getAssignmentDate(), today.plusDays(1)))
            );

            brandingAssignmentRepository.save(assignment);
        }

        return true;
    }

    // Method to manually log a trip and update mileage
    @Transactional
    public TripHistory logTripAndUpdateMileage(String tripId, Long trainId, String routeId,
                                               LocalDateTime startTime, LocalDateTime endTime,
                                               double tripMileage, double loadFactor) {

        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found: " + trainId);
        }

        Train train = trainOpt.get();

        // Create trip history
        TripHistory trip = new TripHistory();
        trip.setTripId(tripId);
        trip.setTrain(train);
        trip.setRouteId(routeId);
        trip.setTripStartTime(startTime);
        trip.setTripEndTime(endTime);
        trip.setTripMileage(tripMileage);
        trip.setPassengerLoadFactor(loadFactor);
        trip.setTripDuration((int) java.time.Duration.between(startTime, endTime).toMinutes());

        TripHistory savedTrip = tripHistoryRepository.save(trip);

        // Update train odometer
        double currentOdometer = train.getCurrentOdometer() != null ? train.getCurrentOdometer() : 0.0;
        train.setCurrentOdometer(currentOdometer + tripMileage);
        trainRepository.save(train);

        // Update branding if trip is today
        if (startTime.toLocalDate().equals(LocalDate.now())) {
            int serviceHours = trip.getTripDuration() / 60;
            updateBrandingExposure(train, tripMileage, serviceHours, List.of(trip));
        }

        return savedTrip;
    }

    // Get daily mileage summary
    public Map<String, Object> getDailyMileageSummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<TripHistory> dayTrips = tripHistoryRepository.findTripsBetween(startOfDay, endOfDay);

        double totalMileage = dayTrips.stream().mapToDouble(TripHistory::getTripMileage).sum();
        int totalTrips = dayTrips.size();
        long uniqueTrains = dayTrips.stream().map(trip -> trip.getTrain().getTrainId()).distinct().count();

        return Map.of(
                "date", date,
                "totalMileage", totalMileage,
                "totalTrips", totalTrips,
                "uniqueTrains", uniqueTrains,
                "averageMileagePerTrip", totalTrips > 0 ? totalMileage / totalTrips : 0.0
        );
    }
}