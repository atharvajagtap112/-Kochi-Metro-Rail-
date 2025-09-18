package com.atharva.kmrlinductionplanningapplication.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "branding_exposure_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandingExposureLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    private TrainBrandingAssignment trainBrandingAssignment;

    private LocalDate logDate;

    private Integer hoursExposed;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}