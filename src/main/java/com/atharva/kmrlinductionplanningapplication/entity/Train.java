package com.atharva.kmrlinductionplanningapplication.entity;

import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trains")
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainId;

    @Column(unique = true, nullable = false)
    private String trainNumber;

    private LocalDate commissioningDate;

    @Enumerated(EnumType.STRING)
    private TrainStatus status;

    private Double currentOdometer;
    private LocalDate lastMaintenanceDate;
    private Double odometerAtLastMaintenance;
    private Double maintenanceInterval = 3500.0;
    private LocalDateTime lastCleaningDateTime;
    private Integer cleaningPeriod = 12;
    private Double dailyMaxMileage = 1000.0;
    private String depotLocation;
    private LocalDateTime lastUpdated;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<JobCard> jobCards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<FitnessCertificate> fitnessCertificates = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<TrainBrandingAssignment> brandingAssignments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<CleaningTask> cleaningTasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "train_id")
    private List<TripHistory> tripHistories = new ArrayList<>();

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public Train() {}

    // Getters and Setters (same as before)
    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public String getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(String trainNumber) {
        this.trainNumber = trainNumber;
    }

    public LocalDate getCommissioningDate() {
        return commissioningDate;
    }

    public void setCommissioningDate(LocalDate commissioningDate) {
        this.commissioningDate = commissioningDate;
    }

    public TrainStatus getStatus() {
        return status;
    }

    public void setStatus(TrainStatus status) {
        this.status = status;
    }

    public Double getCurrentOdometer() {
        return currentOdometer;
    }

    public void setCurrentOdometer(Double currentOdometer) {
        this.currentOdometer = currentOdometer;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(LocalDate lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public Double getOdometerAtLastMaintenance() {
        return odometerAtLastMaintenance;
    }

    public void setOdometerAtLastMaintenance(Double odometerAtLastMaintenance) {
        this.odometerAtLastMaintenance = odometerAtLastMaintenance;
    }

    public Double getMaintenanceInterval() {
        return maintenanceInterval;
    }

    public void setMaintenanceInterval(Double maintenanceInterval) {
        this.maintenanceInterval = maintenanceInterval;
    }

    public LocalDateTime getLastCleaningDateTime() {
        return lastCleaningDateTime;
    }

    public void setLastCleaningDateTime(LocalDateTime lastCleaningDateTime) {
        this.lastCleaningDateTime = lastCleaningDateTime;
    }

    public Integer getCleaningPeriod() {
        return cleaningPeriod;
    }

    public void setCleaningPeriod(Integer cleaningPeriod) {
        this.cleaningPeriod = cleaningPeriod;
    }

    public Double getDailyMaxMileage() {
        return dailyMaxMileage;
    }

    public void setDailyMaxMileage(Double dailyMaxMileage) {
        this.dailyMaxMileage = dailyMaxMileage;
    }

    public String getDepotLocation() {
        return depotLocation;
    }

    public void setDepotLocation(String depotLocation) {
        this.depotLocation = depotLocation;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<JobCard> getJobCards() {
        return jobCards;
    }

    public void setJobCards(List<JobCard> jobCards) {
        this.jobCards = jobCards;
    }

    public List<FitnessCertificate> getFitnessCertificates() {
        return fitnessCertificates;
    }

    public void setFitnessCertificates(List<FitnessCertificate> fitnessCertificates) {
        this.fitnessCertificates = fitnessCertificates;
    }

    public List<TrainBrandingAssignment> getBrandingAssignments() {
        return brandingAssignments;
    }

    public void setBrandingAssignments(List<TrainBrandingAssignment> brandingAssignments) {
        this.brandingAssignments = brandingAssignments;
    }

    public List<CleaningTask> getCleaningTasks() {
        return cleaningTasks;
    }

    public void setCleaningTasks(List<CleaningTask> cleaningTasks) {
        this.cleaningTasks = cleaningTasks;
    }

    public List<TripHistory> getTripHistories() {
        return tripHistories;
    }

    public void setTripHistories(List<TripHistory> tripHistories) {
        this.tripHistories = tripHistories;
    }
}