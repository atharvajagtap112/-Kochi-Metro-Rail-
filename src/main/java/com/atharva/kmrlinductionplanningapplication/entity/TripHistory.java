package com.atharva.kmrlinductionplanningapplication.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "trip_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TripHistory {

    @Id
    private String tripId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

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
}