package com.atharva.kmrlinductionplanningapplication.entity;



import com.atharva.kmrlinductionplanningapplication.enums.JobCardStatus;
import com.atharva.kmrlinductionplanningapplication.enums.Priority;
import com.atharva.kmrlinductionplanningapplication.enums.WorkType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobCard {

    @Id
    private String jobCardId; // e.g., JC-89345

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    private String assetComponent; // e.g., BOGIE-2-BRAKECALIPER

    @Enumerated(EnumType.STRING)
    private WorkType workType;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private JobCardStatus status;

    private LocalDateTime reportedDate;

    private LocalDateTime targetCompletion;

    private LocalDateTime actualStart;

    private LocalDateTime actualEnd;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String technicianNotes;

    private Double laborHours;

    private String assignedTechnicianId;

    private Boolean supervisorOverride = false;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}