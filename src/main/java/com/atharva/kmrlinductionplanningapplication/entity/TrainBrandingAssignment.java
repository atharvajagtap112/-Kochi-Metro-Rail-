package com.atharva.kmrlinductionplanningapplication.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "train_branding_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainBrandingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private BrandingContract brandingContract;

    private LocalDate assignmentDate;

    private Integer totalHoursExposed = 0;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "trainBrandingAssignment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BrandingExposureLog> exposureLogs;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}
