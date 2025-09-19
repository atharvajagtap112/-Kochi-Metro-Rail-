package com.atharva.kmrlinductionplanningapplication.entity;



import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "stabling_geometry")

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


    public StablingGeometry() {}

    // Parameterized constructor
    public StablingGeometry(String bayId, String trackId, Integer positionIndex,
                            BayStatus status, Long currentTrainId, LocalDateTime reservedUntil,
                            String entryPoint, String exitPoint, Integer depthMovesRequired,
                            LocalDateTime lastUpdated) {
        this.bayId = bayId;
        this.trackId = trackId;
        this.positionIndex = positionIndex;
        this.status = status;
        this.currentTrainId = currentTrainId;
        this.reservedUntil = reservedUntil;
        this.entryPoint = entryPoint;
        this.exitPoint = exitPoint;
        this.depthMovesRequired = depthMovesRequired;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getBayId() {
        return bayId;
    }

    public void setBayId(String bayId) {
        this.bayId = bayId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public Integer getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(Integer positionIndex) {
        this.positionIndex = positionIndex;
    }

    public BayStatus getStatus() {
        return status;
    }

    public void setStatus(BayStatus status) {
        this.status = status;
    }

    public Long getCurrentTrainId() {
        return currentTrainId;
    }

    public void setCurrentTrainId(Long currentTrainId) {
        this.currentTrainId = currentTrainId;
    }

    public LocalDateTime getReservedUntil() {
        return reservedUntil;
    }

    public void setReservedUntil(LocalDateTime reservedUntil) {
        this.reservedUntil = reservedUntil;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    public void setEntryPoint(String entryPoint) {
        this.entryPoint = entryPoint;
    }

    public String getExitPoint() {
        return exitPoint;
    }

    public void setExitPoint(String exitPoint) {
        this.exitPoint = exitPoint;
    }

    public Integer getDepthMovesRequired() {
        return depthMovesRequired;
    }

    public void setDepthMovesRequired(Integer depthMovesRequired) {
        this.depthMovesRequired = depthMovesRequired;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


}