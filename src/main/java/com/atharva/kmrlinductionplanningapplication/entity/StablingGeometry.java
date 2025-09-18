package com.atharva.kmrlinductionplanningapplication.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "stabling_geometry")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StablingGeometry {

    @Id
    private String bayId;

    private String trackId;

    private Integer positionIndex;

    @Enumerated(EnumType.STRING)
    private BayStatus status;

    private Long currentTrainId;

    private LocalDateTime reservedUntil;

    private String entryPoint;

    private String exitPoint;

    private Integer depthMovesRequired;

    private LocalDateTime lastUpdated;

    public enum BayStatus {
        OCCUPIED,
        AVAILABLE,
        MAINTENANCE
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}