package com.atharva.kmrlinductionplanningapplication.entity;



import com.atharva.kmrlinductionplanningapplication.enums.WrapType;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "branding_contracts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandingContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    private String advertiserName;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer requiredHours;

    @Enumerated(EnumType.STRING)
    private WrapType wrapType;

    @Column(columnDefinition = "TEXT")
    private String penaltyTerms;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "brandingContract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainBrandingAssignment> trainAssignments;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}