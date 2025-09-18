package com.atharva.kmrlinductionplanningapplication.entity;



import com.atharva.kmrlinductionplanningapplication.enums.CertificateStatus;
import com.atharva.kmrlinductionplanningapplication.enums.Department;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_certificates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FitnessCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Enumerated(EnumType.STRING)
    private Department department;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    private CertificateStatus status;

    private String issuedBy;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}