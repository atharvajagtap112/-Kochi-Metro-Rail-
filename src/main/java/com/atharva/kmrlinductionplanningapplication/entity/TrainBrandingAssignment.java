package com.atharva.kmrlinductionplanningapplication.entity;




import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "train_branding_assignments")
public class TrainBrandingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @Column(name = "train_id", nullable = false)
    private Long trainId;;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private BrandingContract brandingContract;

    @Column(nullable = false)
    private LocalDate assignmentDate;

    private LocalDate removalDate;

    @Column(nullable = false)
    private Integer totalHoursExposed = 0;

    @Column(nullable = false)
    private Double totalMileageExposed = 0.0;

    // Installation details
    private LocalDate installationDate;

    private String installationTeam;

    @Column(columnDefinition = "TEXT")
    private String installationNotes;

    // Quality and condition tracking

    @Enumerated(EnumType.STRING)
    private BrandingCondition brandingCondition = BrandingCondition.EXCELLENT;

    private LocalDate lastInspectionDate;

    @Column(columnDefinition = "TEXT")
    private String conditionNotes;

    // Performance tracking
    private Integer targetDailyHours;

    private Double averageDailyMileage;

    @Enumerated(EnumType.STRING)
    private AssignmentStatus status = AssignmentStatus.ACTIVE;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "trainBrandingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BrandingExposureLog> exposureLogs;

    public enum BrandingCondition {
        EXCELLENT, GOOD, FAIR, POOR, DAMAGED, NEEDS_REPLACEMENT
    }

    public enum AssignmentStatus {
        ACTIVE, PAUSED, COMPLETED, REMOVED
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Default constructor
    public TrainBrandingAssignment() {}

    public TrainBrandingAssignment(Long assignmentId, Long trainId, BrandingContract brandingContract, LocalDate assignmentDate, LocalDate removalDate, Integer totalHoursExposed, Double totalMileageExposed, LocalDate installationDate, String installationTeam, String installationNotes, BrandingCondition brandingCondition, LocalDate lastInspectionDate, String conditionNotes, Integer targetDailyHours, Double averageDailyMileage, AssignmentStatus status, LocalDateTime lastUpdated, List<BrandingExposureLog> exposureLogs) {
        this.assignmentId = assignmentId;
        this.trainId = trainId;
        this.brandingContract = brandingContract;
        this.assignmentDate = assignmentDate;
        this.removalDate = removalDate;
        this.totalHoursExposed = totalHoursExposed;
        this.totalMileageExposed = totalMileageExposed;
        this.installationDate = installationDate;
        this.installationTeam = installationTeam;
        this.installationNotes = installationNotes;
        this.brandingCondition = brandingCondition;
        this.lastInspectionDate = lastInspectionDate;
        this.conditionNotes = conditionNotes;
        this.targetDailyHours = targetDailyHours;
        this.averageDailyMileage = averageDailyMileage;
        this.status = status;
        this.lastUpdated = lastUpdated;
        this.exposureLogs = exposureLogs;
    }

    // Getters and Setters
    public java.lang.Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(java.lang.Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public BrandingContract getBrandingContract() {
        return brandingContract;
    }

    public void setBrandingContract(BrandingContract brandingContract) {
        this.brandingContract = brandingContract;
    }

    public LocalDate getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(LocalDate assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public LocalDate getRemovalDate() {
        return removalDate;
    }

    public void setRemovalDate(LocalDate removalDate) {
        this.removalDate = removalDate;
    }

    public Integer getTotalHoursExposed() {
        return totalHoursExposed;
    }

    public void setTotalHoursExposed(Integer totalHoursExposed) {
        this.totalHoursExposed = totalHoursExposed;
    }

    public Double getTotalMileageExposed() {
        return totalMileageExposed;
    }

    public void setTotalMileageExposed(Double totalMileageExposed) {
        this.totalMileageExposed = totalMileageExposed;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public String getInstallationTeam() {
        return installationTeam;
    }

    public void setInstallationTeam(String installationTeam) {
        this.installationTeam = installationTeam;
    }

    public String getInstallationNotes() {
        return installationNotes;
    }

    public void setInstallationNotes(String installationNotes) {
        this.installationNotes = installationNotes;
    }

    public BrandingCondition getBrandingCondition() {
        return brandingCondition;
    }

    public void setBrandingCondition(BrandingCondition condition) {
        this.brandingCondition = condition;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public String getConditionNotes() {
        return conditionNotes;
    }

    public void setConditionNotes(String conditionNotes) {
        this.conditionNotes = conditionNotes;
    }

    public Integer getTargetDailyHours() {
        return targetDailyHours;
    }

    public void setTargetDailyHours(Integer targetDailyHours) {
        this.targetDailyHours = targetDailyHours;
    }

    public Double getAverageDailyMileage() {
        return averageDailyMileage;
    }

    public void setAverageDailyMileage(Double averageDailyMileage) {
        this.averageDailyMileage = averageDailyMileage;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<BrandingExposureLog> getExposureLogs() {
        return exposureLogs;
    }

    public void setExposureLogs(List<BrandingExposureLog> exposureLogs) {
        this.exposureLogs = exposureLogs;
    }

}