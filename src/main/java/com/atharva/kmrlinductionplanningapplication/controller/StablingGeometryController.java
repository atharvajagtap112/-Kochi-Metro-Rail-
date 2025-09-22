package com.atharva.kmrlinductionplanningapplication.controller;


import com.atharva.kmrlinductionplanningapplication.entity.StablingGeometry;
import com.atharva.kmrlinductionplanningapplication.service.StablingGeometryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stabling")

@CrossOrigin(origins = "*")
public class StablingGeometryController {
     @Autowired
    private StablingGeometryService stablingService;

    @GetMapping("/bays")
    public ResponseEntity<List<StablingGeometry>> getAllBays() {
        List<StablingGeometry> bays = stablingService.getAllBays();
        return ResponseEntity.ok(bays);
    }

    @GetMapping("/bays/{bayId}")
    public ResponseEntity<StablingGeometry> getBayById(@PathVariable String bayId) {
        Optional<StablingGeometry> bay = stablingService.getBayById(bayId);
        return bay.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bays/available")
    public ResponseEntity<List<StablingGeometry>> getAvailableBays() {
        List<StablingGeometry> availableBays = stablingService.getAvailableBays();
        return ResponseEntity.ok(availableBays);
    }

    @GetMapping("/bays/occupied")
    public ResponseEntity<List<StablingGeometry>> getOccupiedBays() {
        List<StablingGeometry> occupiedBays = stablingService.getOccupiedBays();
        return ResponseEntity.ok(occupiedBays);
    }

    @GetMapping("/bays/track/{trackId}")
    public ResponseEntity<List<StablingGeometry>> getBaysByTrack(@PathVariable String trackId) {
        List<StablingGeometry> bays = stablingService.getBaysByTrack(trackId);
        return ResponseEntity.ok(bays);
    }

    @GetMapping("/optimal-assignment")
    public ResponseEntity<Map<String, Object>> getOptimalBayAssignment() {
        Map<String, Object> assignment = stablingService.getOptimalBayAssignment();
        return ResponseEntity.ok(assignment);
    }

    @PostMapping("/bays")
    public ResponseEntity<StablingGeometry> createBay(@RequestBody StablingGeometry bay) {
        StablingGeometry savedBay = stablingService.createBay(bay);
        return ResponseEntity.ok(savedBay);
    }

    @PutMapping("/bays/{bayId}")
    public ResponseEntity<StablingGeometry> updateBay(@PathVariable String bayId,
                                                      @RequestBody StablingGeometry bay) {
        bay.setBayId(bayId);
        StablingGeometry updatedBay = stablingService.updateBay(bay);
        return ResponseEntity.ok(updatedBay);
    }

    @PutMapping("/bays/{bayId}/assign")
    public ResponseEntity<String> assignTrainToBay(@PathVariable String bayId,
                                                   @RequestBody Map<String, Long> request) {
        Long trainId = request.get("trainId");
        boolean success = stablingService.assignTrainToBay(bayId, trainId);
        if (success) {
            return ResponseEntity.ok("Train assigned to bay successfully");
        }
        return ResponseEntity.badRequest().body("Failed to assign train to bay");
    }

    @PutMapping("/bays/{bayId}/release")
    public ResponseEntity<String> releaseTrainFromBay(@PathVariable String bayId) {
        boolean success = stablingService.releaseTrainFromBay(bayId);
        if (success) {
            return ResponseEntity.ok("Train released from bay successfully");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/bays/{bayId}")
    public ResponseEntity<String> deleteBay(@PathVariable String bayId) {
        boolean success = stablingService.deleteBay(bayId);
        if (success) {
            return ResponseEntity.ok("Bay deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
}