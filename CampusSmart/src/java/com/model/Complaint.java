package com.model;

import java.sql.Timestamp;

public class Complaint {
    private int complaintId;
    private String matricNo;
    private String description;
    private String status;
    private Timestamp createdAt;

    // Constructor for creating new complaint (without ID and timestamp)
    public Complaint(String matricNo, String description, String status) {
        this.matricNo = matricNo;
        this.description = description;
        this.status = status;
    }

    // Constructor for retrieving from database (with ID and timestamp)
    public Complaint(int complaintId, String matricNo, String description, String status, Timestamp createdAt) {
        this.complaintId = complaintId;
        this.matricNo = matricNo;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getComplaintId() { return complaintId; }
    public void setComplaintId(int complaintId) { this.complaintId = complaintId; }

    public String getMatricNo() { return matricNo; }
    public void setMatricNo(String matricNo) { this.matricNo = matricNo; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}