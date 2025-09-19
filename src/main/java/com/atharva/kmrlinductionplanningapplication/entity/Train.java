package com.atharva.kmrlinductionplanningapplication.entity;




import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private Double maintenanceInterval = 3500.0; // Default 3500 km

    private LocalDateTime lastCleaningDateTime;

    private Integer cleaningPeriod = 12; // Default 72 hours

    private Double dailyMaxMileage = 1000.0; // Default 1000 km

    private String depotLocation;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobCard> jobCards;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FitnessCertificate> fitnessCertificates;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainBrandingAssignment> brandingAssignments;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CleaningTask> cleaningTasks;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TripHistory> tripHistories;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }


    // Default constructor
    public Train() {}

    // Parameterized constructor
    public Train(Long trainId, String trainNumber, LocalDate commissioningDate,
                 TrainStatus status, Double currentOdometer, LocalDate lastMaintenanceDate,
                 Double odometerAtLastMaintenance, Double maintenanceInterval,
                 LocalDateTime lastCleaningDateTime, Integer cleaningPeriod,
                 Double dailyMaxMileage, String depotLocation, LocalDateTime lastUpdated,
                 List<JobCard> jobCards, List<FitnessCertificate> fitnessCertificates,
                 List<TrainBrandingAssignment> brandingAssignments, List<CleaningTask> cleaningTasks,
                 List<TripHistory> tripHistories) {
        this.trainId = trainId;
        this.trainNumber = trainNumber;
        this.commissioningDate = commissioningDate;
        this.status = status;
        this.currentOdometer = currentOdometer;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.odometerAtLastMaintenance = odometerAtLastMaintenance;
        this.maintenanceInterval = maintenanceInterval;
        this.lastCleaningDateTime = lastCleaningDateTime;
        this.cleaningPeriod = cleaningPeriod;
        this.dailyMaxMileage = dailyMaxMileage;
        this.depotLocation = depotLocation;
        this.lastUpdated = lastUpdated;
        this.jobCards = jobCards;
        this.fitnessCertificates = fitnessCertificates;
        this.brandingAssignments = brandingAssignments;
        this.cleaningTasks = cleaningTasks;
        this.tripHistories = tripHistories;
    }

    // Getters and Setters
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