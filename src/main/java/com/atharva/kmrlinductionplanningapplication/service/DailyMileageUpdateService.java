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
    private TrainRepository trainRepository;
    @Autowired
    private TripHistoryRepository tripHistoryRepository;
    @Autowired
    private TrainBrandingAssignmentRepository brandingAssignmentRepository;
    @Autowired
    private BrandingExposureLogRepository exposureLogRepository;

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

        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59);

        // ✅ Fixed to use trainId
        List<TripHistory> todaysTrips = tripHistoryRepository.findTripsBetween(startOfDay, endOfDay)
                .stream()
                .filter(trip -> trip.getTrainId().equals(trainId))
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

        double todaysMileage = todaysTrips.stream()
                .mapToDouble(TripHistory::getTripMileage)
                .sum();

        int totalServiceMinutes = todaysTrips.stream()
                .mapToInt(TripHistory::getTripDuration)
                .sum();

        int totalServiceHours = totalServiceMinutes / 60;

        double currentOdometer = train.getCurrentOdometer() != null ? train.getCurrentOdometer() : 0.0;
        train.setCurrentOdometer(currentOdometer + todaysMileage);
        trainRepository.save(train);

        // ✅ Fixed branding update
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

    // ✅ Fixed to use trainId
    private boolean updateBrandingExposure(Train train, double todaysMileage, int serviceHours, List<TripHistory> trips) {
        List<TrainBrandingAssignment> activeAssignments = brandingAssignmentRepository
                .findByTrainId(train.getTrainId())
                .stream()
                .filter(assignment -> assignment.getStatus() == TrainBrandingAssignment.AssignmentStatus.ACTIVE)
                .toList();

        if (activeAssignments.isEmpty()) {
            return false;
        }

        LocalDate today = LocalDate.now();

        for (TrainBrandingAssignment assignment : activeAssignments) {
            BrandingExposureLog exposureLog = new BrandingExposureLog();
            exposureLog.setTrainBrandingAssignment(assignment);
            exposureLog.setLogDate(today);
            exposureLog.setHoursExposed(serviceHours);
            exposureLog.setMileageCovered(todaysMileage);

            if (!trips.isEmpty()) {
                exposureLog.setStartOdometer(train.getCurrentOdometer() - todaysMileage);
                exposureLog.setEndOdometer(train.getCurrentOdometer());
            }

            String routesCovered = trips.stream()
                    .map(TripHistory::getRouteId)
                    .distinct()
                    .reduce((r1, r2) -> r1 + ", " + r2)
                    .orElse("No routes");

            exposureLog.setRoutesCovered(routesCovered);
            exposureLog.setTotalTrips(trips.size());

            int totalPassengers = trips.stream()
                    .mapToInt(trip -> (int) (trip.getPassengerLoadFactor() * 100))
                    .sum();

            exposureLog.setPassengerCount(totalPassengers);
            exposureLog.setServiceType(BrandingExposureLog.ServiceType.REGULAR);
            exposureLog.setExposureQuality(BrandingExposureLog.ExposureQuality.NORMAL);
            exposureLog.setWeatherCondition(BrandingExposureLog.WeatherCondition.CLEAR);
            exposureLog.setRemarks("Auto-generated daily exposure log");
            exposureLog.setLoggedBy("SYSTEM");

            exposureLogRepository.save(exposureLog);

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

    // ✅ Fixed to use trainId
    @Transactional
    public TripHistory logTripAndUpdateMileage(String tripId, Long trainId, String routeId,
                                               LocalDateTime startTime, LocalDateTime endTime,
                                               double tripMileage, double loadFactor) {

        Optional<Train> trainOpt = trainRepository.findById(trainId);
        if (trainOpt.isEmpty()) {
            throw new RuntimeException("Train not found: " + trainId);
        }

        Train train = trainOpt.get();

        TripHistory trip = new TripHistory();
        trip.setTripId(tripId);
        trip.setTrainId(trainId); // ✅ Using trainId directly
        trip.setRouteId(routeId);
        trip.setTripStartTime(startTime);
        trip.setTripEndTime(endTime);
        trip.setTripMileage(tripMileage);
        trip.setPassengerLoadFactor(loadFactor);
        trip.setTripDuration((int) java.time.Duration.between(startTime, endTime).toMinutes());

        TripHistory savedTrip = tripHistoryRepository.save(trip);

        double currentOdometer = train.getCurrentOdometer() != null ? train.getCurrentOdometer() : 0.0;
        train.setCurrentOdometer(currentOdometer + tripMileage);
        trainRepository.save(train);

        if (startTime.toLocalDate().equals(LocalDate.now())) {
            int serviceHours = trip.getTripDuration() / 60;
            updateBrandingExposure(train, tripMileage, serviceHours, List.of(trip));
        }

        return savedTrip;
    }

    public Map<String, Object> getDailyMileageSummary(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<TripHistory> dayTrips = tripHistoryRepository.findTripsBetween(startOfDay, endOfDay);

        double totalMileage = dayTrips.stream().mapToDouble(TripHistory::getTripMileage).sum();
        int totalTrips = dayTrips.size();
        long uniqueTrains = dayTrips.stream().map(TripHistory::getTrainId).distinct().count(); // ✅ Fixed

        return Map.of(
                "date", date,
                "totalMileage", totalMileage,
                "totalTrips", totalTrips,
                "uniqueTrains", uniqueTrains,
                "averageMileagePerTrip", totalTrips > 0 ? totalMileage / totalTrips : 0.0
        );
    }
}