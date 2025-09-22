package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.TripHistory;
import com.atharva.kmrlinductionplanningapplication.service.DailyMileageUpdateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/mileage")

@CrossOrigin(origins = "*")
public class MileageUpdateController {
    @Autowired
    private  DailyMileageUpdateService mileageUpdateService;

    @PostMapping("/update-all-trains")
    public ResponseEntity<String> updateAllTrainsMileage() {
        mileageUpdateService.updateDailyMileageForAllTrains();
        return ResponseEntity.ok("All trains mileage updated successfully");
    }

    @PostMapping("/update-train/{trainId}" )
    public ResponseEntity<Map<String, Object>> updateTrainMileage(@PathVariable Long trainId) {
        Map<String, Object> result = mileageUpdateService.updateTrainDailyMileage(trainId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/log-trip")
    public ResponseEntity<TripHistory> logTrip(@RequestBody Map<String, Object> tripData) {
        String tripId = (String) tripData.get("tripId");
        Long trainId = ((Number) tripData.get("trainId")).longValue();
        String routeId = (String) tripData.get("routeId");
        LocalDateTime startTime = LocalDateTime.parse((String) tripData.get("startTime"));
        LocalDateTime endTime = LocalDateTime.parse((String) tripData.get("endTime"));
        Double tripMileage = ((Number) tripData.get("tripMileage")).doubleValue();
        Double loadFactor = ((Number) tripData.get("loadFactor")).doubleValue();

        TripHistory trip = mileageUpdateService.logTripAndUpdateMileage(
                tripId, trainId, routeId, startTime, endTime, tripMileage, loadFactor
        );

        return ResponseEntity.ok(trip);
    }

    @GetMapping("/summary/{date}")
    public ResponseEntity<Map<String, Object>> getDailySummary(@PathVariable String date) {
        LocalDate summaryDate = LocalDate.parse(date);
        Map<String, Object> summary = mileageUpdateService.getDailyMileageSummary(summaryDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/today")
    public ResponseEntity<Map<String, Object>> getTodaysSummary() {
        Map<String, Object> summary = mileageUpdateService.getDailyMileageSummary(LocalDate.now());
        return ResponseEntity.ok(summary);
    }
}