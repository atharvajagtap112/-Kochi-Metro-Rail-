package com.atharva.kmrlinductionplanningapplication.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "branding_exposure_logs")
public class BrandingExposureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;  // ❌ Was Train logId - should be Long!

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private TrainBrandingAssignment trainBrandingAssignment;

    @Column(nullable = false)
    private LocalDate logDate;

    @Column(nullable = false)
    private Integer hoursExposed;

    @Column(nullable = false)
    private Double mileageCovered = 0.0;

    private Double startOdometer;
    private Double endOdometer;
    private String routesCovered;
    private Integer totalTrips;
    private Integer passengerCount;

    private LocalDateTime serviceStartTime;
    private LocalDateTime serviceEndTime;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType = ServiceType.REGULAR;

    @Enumerated(EnumType.STRING)
    private ExposureQuality exposureQuality = ExposureQuality.NORMAL;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(columnDefinition = "TEXT")
    private String routeDetails;

    @Enumerated(EnumType.STRING)
    private WeatherCondition weatherCondition;

    private Double visibilityScore = 1.0;
    private String loggedBy;
    private LocalDateTime lastUpdated;

    public enum ServiceType {
        REGULAR, EXPRESS, SPECIAL, MAINTENANCE_RUN
    }

    public enum ExposureQuality {
        EXCELLENT, GOOD, NORMAL, POOR, NO_EXPOSURE
    }

    public enum WeatherCondition {
        CLEAR, CLOUDY, RAINY, FOGGY, STORMY
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public BrandingExposureLog() {}

    // Getters and Setters
    public Long getLogId() {
        return logId;
    }

    public BrandingExposureLog(Long logId, TrainBrandingAssignment trainBrandingAssignment, LocalDate logDate, Integer hoursExposed, Double mileageCovered, Double startOdometer, Double endOdometer, String routesCovered, Integer totalTrips, Integer passengerCount, LocalDateTime serviceStartTime, LocalDateTime serviceEndTime, ServiceType serviceType, ExposureQuality exposureQuality, String remarks, String routeDetails, WeatherCondition weatherCondition, Double visibilityScore, String loggedBy, LocalDateTime lastUpdated) {
        this.logId = logId;
        this.trainBrandingAssignment = trainBrandingAssignment;
        this.logDate = logDate;
        this.hoursExposed = hoursExposed;
        this.mileageCovered = mileageCovered;
        this.startOdometer = startOdometer;
        this.endOdometer = endOdometer;
        this.routesCovered = routesCovered;
        this.totalTrips = totalTrips;
        this.passengerCount = passengerCount;
        this.serviceStartTime = serviceStartTime;
        this.serviceEndTime = serviceEndTime;
        this.serviceType = serviceType;
        this.exposureQuality = exposureQuality;
        this.remarks = remarks;
        this.routeDetails = routeDetails;
        this.weatherCondition = weatherCondition;
        this.visibilityScore = visibilityScore;
        this.loggedBy = loggedBy;
        this.lastUpdated = lastUpdated;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    // ... rest of getters and setters (same as before but logId is Long)
    public TrainBrandingAssignment getTrainBrandingAssignment() {
        return trainBrandingAssignment;
    }

    public void setTrainBrandingAssignment(TrainBrandingAssignment trainBrandingAssignment) {
        this.trainBrandingAssignment = trainBrandingAssignment;
    }



    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Integer getHoursExposed() {
        return hoursExposed;
    }

    public void setHoursExposed(Integer hoursExposed) {
        this.hoursExposed = hoursExposed;
    }

    public Double getMileageCovered() {
        return mileageCovered;
    }

    public void setMileageCovered(Double mileageCovered) {
        this.mileageCovered = mileageCovered;
    }

    public Double getStartOdometer() {
        return startOdometer;
    }

    public void setStartOdometer(Double startOdometer) {
        this.startOdometer = startOdometer;
    }

    public Double getEndOdometer() {
        return endOdometer;
    }

    public void setEndOdometer(Double endOdometer) {
        this.endOdometer = endOdometer;
    }

    public String getRoutesCovered() {
        return routesCovered;
    }

    public void setRoutesCovered(String routesCovered) {
        this.routesCovered = routesCovered;
    }

    public Integer getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Integer totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Integer getPassengerCount() {
        return passengerCount;
    }

    public void setPassengerCount(Integer passengerCount) {
        this.passengerCount = passengerCount;
    }

    public LocalDateTime getServiceStartTime() {
        return serviceStartTime;
    }

    public void setServiceStartTime(LocalDateTime serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }

    public LocalDateTime getServiceEndTime() {
        return serviceEndTime;
    }

    public void setServiceEndTime(LocalDateTime serviceEndTime) {
        this.serviceEndTime = serviceEndTime;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ExposureQuality getExposureQuality() {
        return exposureQuality;
    }

    public void setExposureQuality(ExposureQuality exposureQuality) {
        this.exposureQuality = exposureQuality;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRouteDetails() {
        return routeDetails;
    }

    public void setRouteDetails(String routeDetails) {
        this.routeDetails = routeDetails;
    }

    public WeatherCondition getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(WeatherCondition weatherCondition) {
        this.weatherCondition = weatherCondition;
    }

    public Double getVisibilityScore() {
        return visibilityScore;
    }

    public void setVisibilityScore(Double visibilityScore) {
        this.visibilityScore = visibilityScore;
    }

    public String getLoggedBy() {
        return loggedBy;
    }

    public void setLoggedBy(String loggedBy) {
        this.loggedBy = loggedBy;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}