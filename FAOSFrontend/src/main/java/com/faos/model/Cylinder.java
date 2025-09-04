package com.faos.model;

import java.time.LocalDateTime;
import java.util.UUID;


public class Cylinder {

    private String id;  
    private CylinderType type;  
    private CylinderStatus status;  
    private LocalDateTime createdDate;  
    private LocalDateTime refillDate;  
    private Double weight;  
    private Supplier supplier; 

    // Constructors
    public Cylinder() {
        this.id = generateAlphanumericId();  // Automatically generate the ID
    }

    public Cylinder(CylinderType type, CylinderStatus status, Double weight,Supplier supplier) {
        this.type = type;
        this.status = status;
        this.weight = weight;
        this.supplier= supplier;
    }
    private String generateAlphanumericId() {
        return "CYL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public CylinderType getType() {
        return type;
    }

    public CylinderStatus getStatus() {
        return status;
    }

    public Double getWeight() {
        return weight;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getRefillDate() {
        return refillDate;
    }

    // Setter methods (No setters for createdDate and refillDate)
    public void setType(CylinderType type) {
        this.type = type;
    }

    public void setStatus(CylinderStatus status) {
        this.status = status;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setSupplier(Supplier supplier2) {
        this.supplier = supplier2;
    }

    public void onCreate() {
        this.createdDate = LocalDateTime.now();  // Set created date on persistence
    }

    public void onUpdate() {
        this.refillDate = LocalDateTime.now();  // Set refill date on update
    }

    // toString method
    @Override
    public String toString() {
        return "Cylinder [id=" + id + ", type=" + type + ", status=" + status 
                + ", createdDate=" + createdDate + ", refillDate=" + refillDate 
                + ", weight=" + weight + "]";
    }
}
