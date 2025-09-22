package com.atharva.kmrlinductionplanningapplication.entity;




import com.atharva.kmrlinductionplanningapplication.enums.JobCardStatus;
import com.atharva.kmrlinductionplanningapplication.enums.Priority;
import com.atharva.kmrlinductionplanningapplication.enums.WorkType;
import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "job_cards")

public class JobCard {

    @Id
    private String jobCardId; // e.g., JC-89345


    @Column(name = "train_id", nullable = false)
    private Long trainId;

    private String trainsetId; // e.g., TS-05 (from external system)

    private String assetComponent; // e.g., BOGIE-1-BRAKECALIPER-RHS

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private JobCardStatus status;

    private LocalDateTime reportedDate;

    private LocalDateTime targetCompletionDate; // Changed from targetCompletion

    private LocalDateTime actualStart;

    private LocalDateTime actualEnd;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String details; // Changed from technicianNotes

    private Double laborHoursLogged; // Changed from laborHours

    private String assignedTo; // Team name instead of individual technician

    private Boolean supervisorOverride = false;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    public JobCard() {}

    // Parameterized constructor
    public JobCard(String jobCardId, Long train, String trainsetId, String assetComponent,
                   WorkType workType, Priority priority, JobCardStatus status,
                   LocalDateTime reportedDate, LocalDateTime targetCompletionDate,
                   LocalDateTime actualStart, LocalDateTime actualEnd, String summary,
                   String details, Double laborHoursLogged, String assignedTo,
                   Boolean supervisorOverride, LocalDateTime lastUpdated) {
        this.jobCardId = jobCardId;
        this.trainId = train;
        this.trainsetId = trainsetId;
        this.assetComponent = assetComponent;
        this.workType = workType;
        this.priority = priority;
        this.status = status;
        this.reportedDate = reportedDate;
        this.targetCompletionDate = targetCompletionDate;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.summary = summary;
        this.details = details;
        this.laborHoursLogged = laborHoursLogged;
        this.assignedTo = assignedTo;
        this.supervisorOverride = supervisorOverride;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getJobCardId() {
        return jobCardId;
    }

    public void setJobCardId(String jobCardId) {
        this.jobCardId = jobCardId;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long train) {
        this.trainId = train;
    }

    public String getTrainsetId() {
        return trainsetId;
    }

    public void setTrainsetId(String trainsetId) {
        this.trainsetId = trainsetId;
    }

    public String getAssetComponent() {
        return assetComponent;
    }

    public void setAssetComponent(String assetComponent) {
        this.assetComponent = assetComponent;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public JobCardStatus getStatus() {
        return status;
    }

    public void setStatus(JobCardStatus status) {
        this.status = status;
    }

    public LocalDateTime getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDateTime reportedDate) {
        this.reportedDate = reportedDate;
    }

    public LocalDateTime getTargetCompletionDate() {
        return targetCompletionDate;
    }

    public void setTargetCompletionDate(LocalDateTime targetCompletionDate) {
        this.targetCompletionDate = targetCompletionDate;
    }

    public LocalDateTime getActualStart() {
        return actualStart;
    }

    public void setActualStart(LocalDateTime actualStart) {
        this.actualStart = actualStart;
    }

    public LocalDateTime getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(LocalDateTime actualEnd) {
        this.actualEnd = actualEnd;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Double getLaborHoursLogged() {
        return laborHoursLogged;
    }

    public void setLaborHoursLogged(Double laborHoursLogged) {
        this.laborHoursLogged = laborHoursLogged;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Boolean getSupervisorOverride() {
        return supervisorOverride;
    }

    public void setSupervisorOverride(Boolean supervisorOverride) {
        this.supervisorOverride = supervisorOverride;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}