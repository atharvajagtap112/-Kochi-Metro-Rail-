package com.atharva.kmrlinductionplanningapplication.controller;




import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import com.atharva.kmrlinductionplanningapplication.service.TrainService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/trains")

@CrossOrigin(origins = "*")
public class TrainController {
    @Autowired
    private  TrainService trainService;




    @GetMapping
    public ResponseEntity<List<Train>> getAllTrains() {
        List<Train> trains = trainService.getAllTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Train> getTrainById(@PathVariable Long id) {
        Optional<Train> train = trainService.getTrainById(id);
        return train.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{trainNumber}")
    public ResponseEntity<Train> getTrainByNumber(@PathVariable String trainNumber) {
        Optional<Train> train = trainService.getTrainByNumber(trainNumber);
        return train.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 🔥 FIXED: Returns detailed response with branding info
    @GetMapping("/available")
    public ResponseEntity<List<Map<String, Object>>> getAvailableTrains() {
        List<Map<String, Object>> availableTrains = trainService.getAvailableTrainsWithDetails();
        return ResponseEntity.ok(availableTrains);
    }

    // 🔥 NEW: Returns simple Train objects for internal use
    @GetMapping("/available/simple")
    public ResponseEntity<List<Train>> getAvailableTrainsSimple() {
        List<Train> availableTrains = trainService.getAvailableTrainsForInduction();
        return ResponseEntity.ok(availableTrains);
    }

    @GetMapping("/maintenance")
    public ResponseEntity<List<Train>> getTrainsInMaintenance() {
        List<Train> maintenanceTrains = trainService.getTrainsInMaintenance();
        return ResponseEntity.ok(maintenanceTrains);
    }

    @PostMapping
    public ResponseEntity<Train> createTrain(@RequestBody Train train) {
        Train savedTrain = trainService.saveTrain(train);
        return ResponseEntity.ok(savedTrain);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<Train>> createAllTrains(@RequestBody List<Train> trains) {
        try {
            if (trains.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            List<Train> savedTrains = trainService.saveAllTrains(trains);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTrains);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create_all")
    public ResponseEntity<Train> createAllTrain(@RequestBody List<Train> train) {
        if (train.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        for (Train train1 : train) {
            trainService.saveTrain(train1);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Train> updateTrain(@PathVariable Long id, @RequestBody Train train) {
        train.setTrainId(id);
        Train updatedTrain = trainService.saveTrain(train);
        return ResponseEntity.ok(updatedTrain);
    }
    @PutMapping("updateStatus/{id}")
    public ResponseEntity<Train> updateTrainStatus(@PathVariable Long id,@RequestParam String status) {
        Optional<Train> train = trainService.getTrainById(id);
        train.get().setStatus(TrainStatus.valueOf(status.trim()));
        Train updatedTrain = trainService.saveTrain(train.get());
        return ResponseEntity.ok(updatedTrain);
    }



    @GetMapping("/{id}/maintenance-due")
    public ResponseEntity<Boolean> isMaintenanceDue(@PathVariable java.lang.Long id) {
        boolean isMaintenanceDue = trainService.isTrainMaintenanceDue(id);
        return ResponseEntity.ok(isMaintenanceDue);
    }

    @GetMapping("/{id}/cleaning-due")
    public ResponseEntity<Boolean> isCleaningDue(@PathVariable java.lang.Long id) {
        boolean isCleaningDue = trainService.isTrainCleaningDue(id);
        return ResponseEntity.ok(isCleaningDue);
    }



    @GetMapping("/{id}/validate-for-service")
    public ResponseEntity<Boolean> validateTrainForService(@PathVariable java.lang.Long id) {
        boolean isValid = trainService.validateTrainForService(id);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/{id}/mileage-balance")
    public ResponseEntity<Double> getMileageBalance(@PathVariable java.lang.Long id) {
        double mileageBalance = trainService.calculateMileageBalance(id);
        return ResponseEntity.ok(mileageBalance);
    }
}