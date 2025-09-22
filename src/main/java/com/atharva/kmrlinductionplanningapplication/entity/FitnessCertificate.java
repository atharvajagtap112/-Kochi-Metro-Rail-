package com.atharva.kmrlinductionplanningapplication.entity;

import com.atharva.kmrlinductionplanningapplication.enums.CertificateStatus;
import com.atharva.kmrlinductionplanningapplication.enums.Department;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "fitness_certificates")
public class FitnessCertificate {

    @Id

    private Long certificateId;

    @Column(name = "train_id", nullable = false)
    private Long trainId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department department;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateStatus status;

    @Column(nullable = false)
    private String issuedBy;

    private String certificateNumber;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(columnDefinition = "TEXT")
    private String inspectionDetails;

    private String approvedBy;
    private LocalDate lastInspectionDate;
    private LocalDate nextInspectionDue;

    @Column(columnDefinition = "TEXT")
    private String complianceNotes;

    private Boolean isRenewal = false;
    private String previousCertificateId;
    private LocalDateTime lastUpdated;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }

    // Constructors
    public FitnessCertificate() {}

    // Getters and Setters
    public Long getCertificateId() {
        return certificateId;
    }


    public Boolean getRenewal() {
        return isRenewal;
    }

    public void setRenewal(Boolean renewal) {
        isRenewal = renewal;
    }

    public FitnessCertificate(Long certificateId, Long trainId, Department department, LocalDate issueDate, LocalDate expiryDate, CertificateStatus status, String issuedBy, String certificateNumber, String remarks, String inspectionDetails, String approvedBy, LocalDate lastInspectionDate, LocalDate nextInspectionDue, String complianceNotes, Boolean isRenewal, String previousCertificateId, LocalDateTime lastUpdated) {
        this.certificateId = certificateId;
        this.trainId = trainId;
        this.department = department;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.issuedBy = issuedBy;
        this.certificateNumber = certificateNumber;
        this.remarks = remarks;
        this.inspectionDetails = inspectionDetails;
        this.approvedBy = approvedBy;
        this.lastInspectionDate = lastInspectionDate;
        this.nextInspectionDue = nextInspectionDue;
        this.complianceNotes = complianceNotes;
        this.isRenewal = isRenewal;
        this.previousCertificateId = previousCertificateId;
        this.lastUpdated = lastUpdated;
    }

    public void setCertificateId(Long certificateId) {
        this.certificateId = certificateId;
    }

    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    // ... rest of getters and setters
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CertificateStatus getStatus() {
        return status;
    }

    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getInspectionDetails() {
        return inspectionDetails;
    }

    public void setInspectionDetails(String inspectionDetails) {
        this.inspectionDetails = inspectionDetails;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public LocalDate getNextInspectionDue() {
        return nextInspectionDue;
    }

    public void setNextInspectionDue(LocalDate nextInspectionDue) {
        this.nextInspectionDue = nextInspectionDue;
    }

    public String getComplianceNotes() {
        return complianceNotes;
    }

    public void setComplianceNotes(String complianceNotes) {
        this.complianceNotes = complianceNotes;
    }

    public Boolean getIsRenewal() {
        return isRenewal;
    }

    public void setIsRenewal(Boolean isRenewal) {
        this.isRenewal = isRenewal;
    }

    public String getPreviousCertificateId() {
        return previousCertificateId;
    }

    public void setPreviousCertificateId(String previousCertificateId) {
        this.previousCertificateId = previousCertificateId;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}