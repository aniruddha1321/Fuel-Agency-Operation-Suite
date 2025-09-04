package com.faos.controller;

import com.faos.exception.InvalidEntityException;
import com.faos.model.Cylinder;
import com.faos.model.Supplier;
import com.faos.service.CylinderService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/cylinders")
@Validated
public class CylinderController {

    private final CylinderService cylinderService;
    private static final Logger logger = LoggerFactory.getLogger(CylinderController.class);

    public CylinderController(CylinderService cylinderService) {
        this.cylinderService = cylinderService;
    }

    // Get all cylinders
    @GetMapping
    public ResponseEntity<List<Cylinder>> getAllCylinders() throws InvalidEntityException {
        logger.info("Fetching all cylinders");
        try {
            return ResponseEntity.ok(cylinderService.getAllCylinders());
        } catch (Exception e) {
            logger.error("Failed to fetch cylinders: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch cylinders", e);
        }
    }

    // Add a new cylinder
    @PostMapping("/{supplierId}")
    public ResponseEntity<Cylinder> addCylinder(@Valid @RequestBody Cylinder cylinder, @PathVariable String supplierId) {
        logger.info("Adding a new cylinder: {}", cylinder);
        try {
            Cylinder savedCylinder = cylinderService.addCylinder(cylinder,supplierId);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCylinder);
        } catch (ResponseStatusException e) {
            logger.error("Error adding cylinder: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Failed to add cylinder: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to add cylinder", e);
        }
    }

    // Get a cylinder by ID
    @GetMapping("/{id}")
    public ResponseEntity<Cylinder> getCylinderById(@PathVariable String id) {
        logger.info("Fetching cylinder with ID: {}", id);
        try {
            Cylinder cylinder = cylinderService.getCylinderById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cylinder not found"));
            return ResponseEntity.ok(cylinder);
        } catch (Exception e) {
            logger.error("Failed to fetch cylinder: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch cylinder", e);
        }
    }

    // Update a cylinder
    @PutMapping("/{id}")
    public ResponseEntity<Cylinder> updateCylinder(@PathVariable String id, @Valid @RequestBody Cylinder updatedCylinder) {
        logger.info("Updating cylinder with ID: {}", id);
        try {
            Cylinder savedCylinder = cylinderService.updateCylinder(id, updatedCylinder);
            return ResponseEntity.ok(savedCylinder);
        } catch (Exception e) {
            logger.error("Failed to update cylinder: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update cylinder", e);
        }
    }

    // Delete a cylinder
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCylinder(@PathVariable String id) {
        logger.info("Deleting cylinder with ID: {}", id);
        try {
            cylinderService.deleteCylinder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Failed to delete cylinder: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete cylinder", e);
        }
    }

    // Supplier-related endpoints
    @GetMapping("/suppliers")
    public ResponseEntity<List<Supplier>> getAllSuppliers() {
        logger.info("Fetching all suppliers");
        try {
            return ResponseEntity.ok(cylinderService.getAllSuppliers());
        } catch (Exception e) {
            logger.error("Failed to fetch suppliers: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch suppliers", e);
        }
    }

    @GetMapping("/suppliers/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable String id) {
        logger.info("Fetching supplier with ID: {}", id);
        try {
            Supplier supplier = cylinderService.getSupplierById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found"));
            return ResponseEntity.ok(supplier);
        } catch (Exception e) {
            logger.error("Failed to fetch supplier: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch supplier", e);
        }
    }
    
    @PostMapping("/refill/{id}")
    public ResponseEntity<?> refillCylinder(@PathVariable String id) {
        try {
            Cylinder updatedCylinder = cylinderService.refillCylinder(id);
            return ResponseEntity.ok(updatedCylinder);
        } catch (InvalidEntityException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/empty/available")
    public ResponseEntity<List<Cylinder>> emptyAndAvailableCylinders() {
    	logger.info("Fetching empty and available cylinders");
        try {
            return ResponseEntity.ok(cylinderService.getAllEmptyAvailableCylinders());
        } catch (Exception e) {
            logger.error("Failed to fetch cylinders: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch cylinders", e);
        }
    }


}
