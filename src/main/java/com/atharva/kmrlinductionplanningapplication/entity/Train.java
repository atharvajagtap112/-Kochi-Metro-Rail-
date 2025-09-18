package com.atharva.kmrlinductionplanningapplication.entity;




import com.atharva.kmrlinductionplanningapplication.enums.TrainStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "trains")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trainId;

    @Column(unique = true, nullable = false)
    private String trainNumber;

    private LocalDate commissioningDate;

    @Enumerated(EnumType.STRING)
    private TrainStatus status;

    private Double currentOdometer;

    private LocalDate lastMaintenanceDate;

    private Double odometerAtLastMaintenance;

    private Double maintenanceInterval = 3500.0; // Default 3500 km

    private LocalDateTime lastCleaningDateTime;

    private Integer cleaningPeriod = 72; // Default 72 hours

    private Double dailyMaxMileage = 1000.0; // Default 1000 km

    private String depotLocation;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JobCard> jobCards;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FitnessCertificate> fitnessCertificates;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainBrandingAssignment> brandingAssignments;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CleaningTask> cleaningTasks;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TripHistory> tripHistories;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}