package com.faos.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.exception.InvalidEntityException;
import com.faos.model.Cylinder;
import com.faos.model.CylinderStatus;
import com.faos.model.CylinderType;
import com.faos.model.Supplier;
import com.faos.repository.CylinderRepository;
import com.faos.repository.SupplierRepository;

import jakarta.transaction.Transactional;

@Service
public class CylinderService {

    @Autowired
    private CylinderRepository cylinderRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
 // Get all cylinders
    public List<Cylinder> getAllCylinders() throws InvalidEntityException {
        try {
            return cylinderRepository.findAll();
        } catch (Exception e) {
            throw new InvalidEntityException("Error retrieving cylinders");
        }
    }

    // Add a new cylinder
    public Cylinder addCylinder(Cylinder cylinder, String suppId) throws InvalidEntityException {
        
        if (cylinder.getWeight() == null || cylinder.getWeight() <= 0) {
            throw new InvalidEntityException("Cylinder weight must be a positive number");
        }
        // Save and return cylinder
        try {
        	cylinder.setStatus(CylinderStatus.AVAILABLE);
        	cylinder.setType(CylinderType.EMPTY);
        	Optional<Supplier> suppOptional = supplierRepository.findById(suppId);
        	if (suppOptional.isPresent()) {
                cylinder.setSupplier(suppOptional.get());
                return cylinderRepository.save(cylinder);
            }else {
                throw new InvalidEntityException("Project Id does not exist");
            }
            
        } catch (Exception e) {
        	e.printStackTrace();  
            throw new InvalidEntityException("Error adding cylinder");
        }
    }

    // Get a cylinder by ID
    public Optional<Cylinder> getCylinderById(String id) throws InvalidEntityException {
        try {
            if (!cylinderRepository.existsById(id)) {
                throw new InvalidEntityException("Cylinder not found");
            }
            return cylinderRepository.findById(id);
        } catch (Exception e) {
            throw new InvalidEntityException("Error retrieving cylinder by ID");
        }
    }

    // Update an existing cylinder
    public Cylinder updateCylinder(String id, Cylinder updatedCylinder) throws InvalidEntityException {
    	return cylinderRepository.findById(id)
    	        .map(existingCylinder -> {
    	            existingCylinder.setType(updatedCylinder.getType());
    	            existingCylinder.setStatus(updatedCylinder.getStatus());
    	            existingCylinder.setWeight(updatedCylinder.getWeight());

    	            if (updatedCylinder.getSupplier() != null) {
    	                String supplierId = updatedCylinder.getSupplier().getSupplierID();
    	                Optional<Supplier> supplierOpt = supplierRepository.findById(supplierId);
    	                if (supplierOpt.isPresent()) {
    	                    existingCylinder.setSupplier(supplierOpt.get());
    	                } else {
    	                    throw new InvalidEntityException("Supplier not found");
    	                }
    	            }

    	            return cylinderRepository.save(existingCylinder);
    	        })
    	        .orElseThrow(() -> new InvalidEntityException("Cylinder not found"));
    }


    // Delete a cylinder
    public void deleteCylinder(String id) throws InvalidEntityException {
        try {
            if (!cylinderRepository.existsById(id)) {
                throw new InvalidEntityException("Cylinder not found");
            }
            cylinderRepository.deleteById(id);
        } catch (Exception e) {
            throw new InvalidEntityException("Error deleting cylinder");
        }
    }

    // Get cylinders by supplier ID
    public List<Cylinder> getCylindersBySupplier(String supplierID) throws InvalidEntityException {
        try {
            return cylinderRepository.findBySupplierId(supplierID);
        } catch (Exception e) {
            throw new InvalidEntityException("Error retrieving cylinders by supplier");
        }
    }

    // Get all suppliers
    public List<Supplier> getAllSuppliers() {
        return StreamSupport.stream(cylinderRepository.findAll().spliterator(), false)
                            .map(Cylinder::getSupplier)
                            .distinct()
                            .collect(Collectors.toList());
    }

 // Get a supplier by ID
    public Optional<Supplier> getSupplierById(String id) {
        return StreamSupport.stream(cylinderRepository.findAll().spliterator(), false)
                            .map(Cylinder::getSupplier)
                            .filter(supplier -> supplier.getSupplierID().equals(id))
                            .findFirst();
    }
    
    
    

    // Method to get cylinder by type
    public String getCylinderId(String type) {
        return cylinderRepository.findByConType(type).orElse(null);
    }

    // Method to update cylinder status
    public void updateCylinderStatus(String cylinderid) {
        cylinderRepository.updateCylinderStatus(cylinderid);
    }

    // Method to get cylinder by ID
    public Optional<Cylinder> getCustomerById(String cylinderid) {
        return cylinderRepository.findById(cylinderid);
    }

    // Method to set booking ID on a cylinder, with proper transaction handling
    @Transactional
    public void setBookingId(String cylinderid, Long bookingId) {
        int updatedRows = cylinderRepository.updateCylinderBookingId(cylinderid, bookingId);
        if (updatedRows == 0) {
            throw new RuntimeException("Failed to update cylinder bookingId");
        }
    }

    // Method to update cylinder status back to 'Available'
    public void updateCylinder(long bookingId) {
        System.out.println(bookingId);
        cylinderRepository.updateCylinder(bookingId);
    }
    
    @Transactional
    public Cylinder refillCylinder(String id) throws InvalidEntityException {
        Cylinder cylinder = cylinderRepository.findById(id)
            .orElseThrow(() -> new InvalidEntityException("Cylinder not found"));

        // Condition: Only refill if EMPTY & AVAILABLE
        if (cylinder.getType() == CylinderType.EMPTY && cylinder.getStatus() == CylinderStatus.AVAILABLE) {
            cylinder.setType(CylinderType.FULL);
            cylinder.setRefillDate(LocalDateTime.now());  // Update refill date
            return cylinderRepository.save(cylinder);
        } else {
            throw new InvalidEntityException("Cylinder is not eligible for refill.");
        }
    }
    
    public List<Cylinder> getAllEmptyAvailableCylinders() throws InvalidEntityException{
    	try {
            return cylinderRepository.findAllEmptyAvailableCylinders();
        } catch (Exception e) {
            throw new InvalidEntityException("Error retrieving cylinders");
        }
    }
}
