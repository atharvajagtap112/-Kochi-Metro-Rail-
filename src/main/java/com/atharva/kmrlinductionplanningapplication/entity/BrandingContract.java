package com.atharva.kmrlinductionplanningapplication.entity;



import com.atharva.kmrlinductionplanningapplication.enums.BrandingType;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "branding_contracts")

public class BrandingContract {

    @Id

    private Long contractId;

    @Column(nullable = false)
    private String advertiserName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private Integer requiredHours;

    // 🔥 NEW: Support multiple branding types
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BrandingType brandingType;

    @Column(columnDefinition = "TEXT")
    private String brandingDescription;

    // Contract financial details
    @Column(precision = 10, scale = 2)
    private BigDecimal contractValue;

    @Column(precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(columnDefinition = "TEXT")
    private String penaltyTerms;

    @Column(precision = 5, scale = 2)
    private BigDecimal penaltyPercentage;

    // SLA details
    private Integer minimumDailyHours;

    private Integer minimumWeeklyHours;

    @Column(columnDefinition = "TEXT")
    private String slaRequirements;

    // Content and placement details
    @Column(columnDefinition = "TEXT")
    private String creativeContent;

    @Column(columnDefinition = "TEXT")
    private String placementInstructions;

    // Additional details
    private String contactPerson;

    private String contactEmail;

    private String contactPhone;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus = ContractStatus.ACTIVE;

    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "brandingContract", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TrainBrandingAssignment> trainAssignments;

    public enum ContractStatus {
        ACTIVE, PAUSED, COMPLETED, CANCELLED
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
    public BrandingContract() {};

    public BrandingContract(Long contractId, String advertiserName, LocalDate startDate, LocalDate endDate, Integer requiredHours, BrandingType brandingType, String brandingDescription, BigDecimal contractValue, BigDecimal hourlyRate, String penaltyTerms, BigDecimal penaltyPercentage, Integer minimumDailyHours, Integer minimumWeeklyHours, String slaRequirements, String creativeContent, String placementInstructions, String contactPerson, String contactEmail, String contactPhone, ContractStatus contractStatus, LocalDateTime lastUpdated, List<TrainBrandingAssignment> trainAssignments) {
        this.contractId = contractId;
        this.advertiserName = advertiserName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.requiredHours = requiredHours;
        this.brandingType = brandingType;
        this.brandingDescription = brandingDescription;
        this.contractValue = contractValue;
        this.hourlyRate = hourlyRate;
        this.penaltyTerms = penaltyTerms;
        this.penaltyPercentage = penaltyPercentage;
        this.minimumDailyHours = minimumDailyHours;
        this.minimumWeeklyHours = minimumWeeklyHours;
        this.slaRequirements = slaRequirements;
        this.creativeContent = creativeContent;
        this.placementInstructions = placementInstructions;
        this.contactPerson = contactPerson;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.contractStatus = contractStatus;
        this.lastUpdated = lastUpdated;
        this.trainAssignments = trainAssignments;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getRequiredHours() {
        return requiredHours;
    }

    public void setRequiredHours(Integer requiredHours) {
        this.requiredHours = requiredHours;
    }

    public BrandingType getBrandingType() {
        return brandingType;
    }

    public void setBrandingType(BrandingType brandingType) {
        this.brandingType = brandingType;
    }

    public String getBrandingDescription() {
        return brandingDescription;
    }

    public void setBrandingDescription(String brandingDescription) {
        this.brandingDescription = brandingDescription;
    }

    public BigDecimal getContractValue() {
        return contractValue;
    }

    public void setContractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public String getPenaltyTerms() {
        return penaltyTerms;
    }

    public void setPenaltyTerms(String penaltyTerms) {
        this.penaltyTerms = penaltyTerms;
    }

    public BigDecimal getPenaltyPercentage() {
        return penaltyPercentage;
    }

    public void setPenaltyPercentage(BigDecimal penaltyPercentage) {
        this.penaltyPercentage = penaltyPercentage;
    }

    public Integer getMinimumDailyHours() {
        return minimumDailyHours;
    }

    public void setMinimumDailyHours(Integer minimumDailyHours) {
        this.minimumDailyHours = minimumDailyHours;
    }

    public Integer getMinimumWeeklyHours() {
        return minimumWeeklyHours;
    }

    public void setMinimumWeeklyHours(Integer minimumWeeklyHours) {
        this.minimumWeeklyHours = minimumWeeklyHours;
    }

    public String getSlaRequirements() {
        return slaRequirements;
    }

    public void setSlaRequirements(String slaRequirements) {
        this.slaRequirements = slaRequirements;
    }

    public String getCreativeContent() {
        return creativeContent;
    }

    public void setCreativeContent(String creativeContent) {
        this.creativeContent = creativeContent;
    }

    public String getPlacementInstructions() {
        return placementInstructions;
    }

    public void setPlacementInstructions(String placementInstructions) {
        this.placementInstructions = placementInstructions;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public ContractStatus getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(ContractStatus contractStatus) {
        this.contractStatus = contractStatus;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<TrainBrandingAssignment> getTrainAssignments() {
        return trainAssignments;
    }

    public void setTrainAssignments(List<TrainBrandingAssignment> trainAssignments) {
        this.trainAssignments = trainAssignments;
    }
}