package com.atharva.kmrlinductionplanningapplication.service;


import com.atharva.kmrlinductionplanningapplication.entity.StablingGeometry;
import com.atharva.kmrlinductionplanningapplication.entity.Train;
import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import com.atharva.kmrlinductionplanningapplication.repository.StablingGeometryRepository;
import com.atharva.kmrlinductionplanningapplication.repository.TrainRepository;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class StablingGeometryService {

    private  StablingGeometryRepository stablingRepository;
    private  TrainRepository trainRepository;

    public StablingGeometryService(StablingGeometryRepository stablingRepository, TrainRepository trainRepository) {
        this.stablingRepository = stablingRepository;
        this.trainRepository = trainRepository;
    }

    public List<StablingGeometry> getAllBays() {
        return stablingRepository.findAll();
    }

    public Optional<StablingGeometry> getBayById(String bayId) {
        return stablingRepository.findById(bayId);
    }

    public List<StablingGeometry> getAvailableBays() {
        return stablingRepository.findByStatus(StablingGeometry.BayStatus.AVAILABLE);
    }

    public List<StablingGeometry> getOccupiedBays() {
        return stablingRepository.findByStatus(StablingGeometry.BayStatus.OCCUPIED);
    }

    public List<StablingGeometry> getBaysByTrack(String trackId) {
        return stablingRepository.findByTrackId(trackId);
    }

    public StablingGeometry createBay(StablingGeometry bay) {
        bay.setStatus(StablingGeometry.BayStatus.AVAILABLE);
        return stablingRepository.save(bay);
    }

    public StablingGeometry updateBay(StablingGeometry bay) {
        return stablingRepository.save(bay);
    }

    public boolean assignTrainToBay(String bayId, Long trainId) {
        Optional<StablingGeometry> bayOpt = stablingRepository.findById(bayId);
        Optional<Train> trainOpt = trainRepository.findById(trainId);

        if (bayOpt.isEmpty() || trainOpt.isEmpty()) {
            return false;
        }

        StablingGeometry bay = bayOpt.get();

        if (bay.getStatus() != StablingGeometry.BayStatus.AVAILABLE) {
            return false; // Bay is not available
        }

        bay.setStatus(StablingGeometry.BayStatus.OCCUPIED);
        bay.setCurrentTrainId(trainId);
        stablingRepository.save(bay);

        return true;
    }

    public boolean releaseTrainFromBay(String bayId) {
        Optional<StablingGeometry> bayOpt = stablingRepository.findById(bayId);

        if (bayOpt.isEmpty()) {
            return false;
        }

        StablingGeometry bay = bayOpt.get();
        bay.setStatus(StablingGeometry.BayStatus.AVAILABLE);
        bay.setCurrentTrainId(null);
        bay.setReservedUntil(null);
        stablingRepository.save(bay);

        return true;
    }

    public boolean deleteBay(String bayId) {
        if (stablingRepository.existsById(bayId)) {
            stablingRepository.deleteById(bayId);
            return true;
        }
        return false;
    }

    public Map<String, Object> getOptimalBayAssignment() {
        List<StablingGeometry> availableBays = getAvailableBays();
        List<Train> availableTrains = trainRepository.findAvailableTrainsWithNoOpenJobCards(
               TrainStatus.ACTIVE
        );

        Map<String, Object> assignment = new HashMap<>();
        assignment.put("availableBays", availableBays.size());
        assignment.put("availableTrains", availableTrains.size());
        assignment.put("canAccommodate", availableBays.size() >= availableTrains.size());

        // Sort bays by minimal depth moves required (optimal positioning)
        List<StablingGeometry> optimalBays = availableBays.stream()
                .sorted((b1, b2) -> Integer.compare(
                        b1.getDepthMovesRequired() != null ? b1.getDepthMovesRequired() : 0,
                        b2.getDepthMovesRequired() != null ? b2.getDepthMovesRequired() : 0
                ))
                .toList();

        assignment.put("recommendedBays", optimalBays);
        assignment.put("totalShuntingMoves", calculateTotalShuntingMoves(optimalBays));

        return assignment;
    }

    private int calculateTotalShuntingMoves(List<StablingGeometry> bays) {
        return bays.stream()
                .mapToInt(bay -> bay.getDepthMovesRequired() != null ? bay.getDepthMovesRequired() : 0)
                .sum();
    }
}