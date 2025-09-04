package com.faos.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class Cylinder {

    @Id
    @Column(name = "id", unique = true)
    private String id;  // Corresponds to VARCHAR in DB

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Cylinder type is required")
    @Column(name = "cylinder_type", nullable = false)
    private CylinderType type;  // Enums for Cylinder Type

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Cylinder status is required")
    @Column(name = "status", nullable = false)
    private CylinderStatus status;  // Enums for Cylinder Status

    @Column(name = "created_date", nullable = true)
    private LocalDateTime createdDate;  // LocalDateTime for DATETIME

    @Column(name = "refill_date", nullable = true)
    private LocalDateTime refillDate;  // LocalDateTime for DATETIME

    @NotNull(message = "Cylinder weight is required")
    @Min(value = 0, message = "Cylinder weight must be a positive number")
    @Column(name = "weight", nullable = false)
    private Double weight;  // Double for numeric values

    // Many-to-one relationship with Supplier (Mapping foreign key to supplier_id)
    
    @ManyToOne
    @JoinColumn(name = "supplierID", nullable = false)  // Foreign key column in DB
    @JsonIgnoreProperties("cylinderList")
    private Supplier supplier;  // Supplier associated with the cylinder


    
    @JsonIgnoreProperties
    @OneToOne
    @JoinColumn(name = "bookingId", referencedColumnName = "bookingId", nullable = true)
    private Booking booking;


    // Constructors
    public Cylinder() {
        this.id = generateAlphanumericId();  // Automatically generate the ID
    }

    public Cylinder(CylinderType type, CylinderStatus status, Double weight,Supplier supplier) {
        this();
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
    
    public void setRefillDate(LocalDateTime date) {
        this.refillDate = date;
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
    
    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    // Callback methods for entity lifecycle
    @PrePersist
    public void onCreate() {
        this.createdDate = LocalDateTime.now();  // Set created date on persistence
    }

    @PreUpdate
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
