package com.atharva.kmrlinductionplanningapplication.entity;


import com.atharva.kmrlinductionplanningapplication.enums.CleaningType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "cleaning_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CleaningTask {

    @Id
    private String taskId; // e.g., CLN-2025-0916-001

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

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
        SCHEDULED,
        IN_PROGRESS,
        DONE,
        CANCELLED
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}