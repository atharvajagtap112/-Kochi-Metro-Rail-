package com.atharva.kmrlinductionplanningapplication.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "trip_history")
public class TripHistory {

    @Id
    private String tripId;

    @Column(name = "train_id", nullable = false)
    private Long trainId;

    private String routeId;
    private LocalDateTime tripStartTime;
    private LocalDateTime tripEndTime;
    private Double routeDistance;
    private Integer tripDuration; // in minutes
    private String crewId;
    private LocalDateTime stablingStartTime;
    private LocalDateTime stablingEndTime;
    private Double tripMileage;
    private Double passengerLoadFactor;
    private Double energyConsumption;
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public TripHistory() {}

    public TripHistory(String tripId, Long trainId, String routeId, LocalDateTime tripStartTime,
                       LocalDateTime tripEndTime, Double routeDistance, Integer tripDuration,
                       String crewId, LocalDateTime stablingStartTime, LocalDateTime stablingEndTime,
                       Double tripMileage, Double passengerLoadFactor, Double energyConsumption,
                       LocalDateTime lastUpdated) {
        this.tripId = tripId;
        this.trainId = trainId;
        this.routeId = routeId;
        this.tripStartTime = tripStartTime;
        this.tripEndTime = tripEndTime;
        this.routeDistance = routeDistance;
        this.tripDuration = tripDuration;
        this.crewId = crewId;
        this.stablingStartTime = stablingStartTime;
        this.stablingEndTime = stablingEndTime;
        this.tripMileage = tripMileage;
        this.passengerLoadFactor = passengerLoadFactor;
        this.energyConsumption = energyConsumption;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getTripId() { return tripId; }
    public void setTripId(String tripId) { this.tripId = tripId; }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public LocalDateTime getTripStartTime() { return tripStartTime; }
    public void setTripStartTime(LocalDateTime tripStartTime) { this.tripStartTime = tripStartTime; }

    public LocalDateTime getTripEndTime() { return tripEndTime; }
    public void setTripEndTime(LocalDateTime tripEndTime) { this.tripEndTime = tripEndTime; }

    public Double getRouteDistance() { return routeDistance; }
    public void setRouteDistance(Double routeDistance) { this.routeDistance = routeDistance; }

    public Integer getTripDuration() { return tripDuration; }
    public void setTripDuration(Integer tripDuration) { this.tripDuration = tripDuration; }

    public String getCrewId() { return crewId; }
    public void setCrewId(String crewId) { this.crewId = crewId; }

    public LocalDateTime getStablingStartTime() { return stablingStartTime; }
    public void setStablingStartTime(LocalDateTime stablingStartTime) { this.stablingStartTime = stablingStartTime; }

    public LocalDateTime getStablingEndTime() { return stablingEndTime; }
    public void setStablingEndTime(LocalDateTime stablingEndTime) { this.stablingEndTime = stablingEndTime; }

    public Double getTripMileage() { return tripMileage; }
    public void setTripMileage(Double tripMileage) { this.tripMileage = tripMileage; }

    public Double getPassengerLoadFactor() { return passengerLoadFactor; }
    public void setPassengerLoadFactor(Double passengerLoadFactor) { this.passengerLoadFactor = passengerLoadFactor; }

    public Double getEnergyConsumption() { return energyConsumption; }
    public void setEnergyConsumption(Double energyConsumption) { this.energyConsumption = energyConsumption; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}