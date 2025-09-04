package com.faos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.faos.exception.InvalidEntityException;
import com.faos.model.Supplier;
import com.faos.repository.SupplierRepository;

@Service
public class SupplierService {
	@Autowired
    private SupplierRepository repository;
	
	@Autowired
	private EmailService emailService;
	
	//to automatically generate supplierID
	private synchronized String generateSupplierId() {
        // Get the highest existing ID
        String maxId = repository.findMaxSupplierId();
        if (maxId == null) {
            return "SUPP001"; // Start with supp001 if no suppliers exist
        }

        // Extract the numeric part and increment it
        int numericPart = Integer.parseInt(maxId.substring(4));
        numericPart++;
        if(numericPart>1 && numericPart<9) return "SUPP"+"00"+numericPart;
        else if(numericPart>9 && numericPart<99) return "SUPP"+"0"+numericPart;
        else return "SUPP" + numericPart;
    }

	//add suppplier
    public Supplier addSupplier(Supplier supplier){
    	if (supplier.getSupplierID() == null || supplier.getSupplierID().isEmpty()) {
            String newSupplierId = generateSupplierId();
            supplier.setSupplierID(newSupplierId);
        }
		supplier.setActive(true);
		return repository.save(supplier);
    }

    // get all suppliers
    public List<Supplier> getAllSuppliers() {
        return repository.findAll();
    }
    

    //get active supppliers
    public List<Supplier> getActiveSuppliers() {
        return repository.findActiveSuppliers();
    }
    
    //get inactive suppliers
    public List<Supplier> getInActiveSuppliers() {
        return repository.findInActiveSuppliers();
    }

    //get supplier by id
    public Supplier getSupplierById(String id) throws InvalidEntityException{
        return repository.findById(id).orElseThrow(()-> new InvalidEntityException("supplier id doesn't exist"));
    }
    
    //update supplier
    public Supplier updateSupplier(String id, Supplier supplier) {
    	Supplier existingSupplier = repository.findById(id).orElseThrow(()-> new RuntimeException("Supplier not found with id: "+id));
    	
    	if(existingSupplier.isActive()) supplier.setActive(true);
    	else existingSupplier.setActive(false);
    	existingSupplier.setName(supplier.getName());
    	existingSupplier.setAddress(supplier.getAddress());
    	existingSupplier.setContact(supplier.getContact());
        return repository.save(existingSupplier);
    }
    
 
    //deactivate supplier
    public boolean deactivateSupplier(String id) throws InvalidEntityException {
    	Supplier supp = repository.findById(id).orElseThrow(() -> 
        new InvalidEntityException("Supplier id doesn't exist")
    );

    if (!supp.isActive()) {
        throw new InvalidEntityException("Supplier is already deactivated.");
    }

    supp.setActive(false);
    repository.save(supp);

    // Send email notification to admin
    String adminEmail = "acharykeerti@gmail.com"; // Replace with actual admin email
    String subject = "Supplier Deactivation Notification";
    String body = "Supplier with ID " + id + " (" + supp.getName() + ") has been deactivated.";
    emailService.sendEmail(adminEmail, subject, body);

    return true;

        
    }

    
	//reactivate suppplier
	public Supplier reactivateSupplier(String id) throws InvalidEntityException {
		Supplier supp= repository.findById(id).orElseThrow(()-> new InvalidEntityException("Supplier id doesn't exist"));
		supp.setActive(true);
		return repository.save(supp);
	}

}
