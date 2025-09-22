package com.atharva.kmrlinductionplanningapplication.entity;

import com.atharva.kmrlinductionplanningapplication.enums.CleaningType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cleaning_tasks")
public class CleaningTask {

    @Id
    private String taskId; // e.g., CLN-2025-0916-001

    @Column(name = "train_id", nullable = false)
    private Long trainId;

    private String bayId;

    @Enumerated(EnumType.STRING)
    private CleaningType cleaningType;

    private LocalDateTime scheduledStart;
    private LocalDateTime scheduledEnd;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private String assignedTeamId;
    private Boolean supervisorOverride = false;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime lastUpdated;

    public enum TaskStatus {
        SCHEDULED, IN_PROGRESS, DONE, CANCELLED
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public CleaningTask() {}

    public CleaningTask(String taskId, Long trainId, String bayId, CleaningType cleaningType,
                        LocalDateTime scheduledStart, LocalDateTime scheduledEnd,
                        LocalDateTime actualStart, LocalDateTime actualEnd, TaskStatus status,
                        String assignedTeamId, Boolean supervisorOverride, String remarks,
                        LocalDateTime lastUpdated) {
        this.taskId = taskId;
        this.trainId = trainId;
        this.bayId = bayId;
        this.cleaningType = cleaningType;
        this.scheduledStart = scheduledStart;
        this.scheduledEnd = scheduledEnd;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.status = status;
        this.assignedTeamId = assignedTeamId;
        this.supervisorOverride = supervisorOverride;
        this.remarks = remarks;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }

    public Long getTrainId() { return trainId; }
    public void setTrainId(Long trainId) { this.trainId = trainId; }

    public String getBayId() { return bayId; }
    public void setBayId(String bayId) { this.bayId = bayId; }

    public CleaningType getCleaningType() { return cleaningType; }
    public void setCleaningType(CleaningType cleaningType) { this.cleaningType = cleaningType; }

    public LocalDateTime getScheduledStart() { return scheduledStart; }
    public void setScheduledStart(LocalDateTime scheduledStart) { this.scheduledStart = scheduledStart; }

    public LocalDateTime getScheduledEnd() { return scheduledEnd; }
    public void setScheduledEnd(LocalDateTime scheduledEnd) { this.scheduledEnd = scheduledEnd; }

    public LocalDateTime getActualStart() { return actualStart; }
    public void setActualStart(LocalDateTime actualStart) { this.actualStart = actualStart; }

    public LocalDateTime getActualEnd() { return actualEnd; }
    public void setActualEnd(LocalDateTime actualEnd) { this.actualEnd = actualEnd; }

    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }

    public String getAssignedTeamId() { return assignedTeamId; }
    public void setAssignedTeamId(String assignedTeamId) { this.assignedTeamId = assignedTeamId; }

    public Boolean getSupervisorOverride() { return supervisorOverride; }
    public void setSupervisorOverride(Boolean supervisorOverride) { this.supervisorOverride = supervisorOverride; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}