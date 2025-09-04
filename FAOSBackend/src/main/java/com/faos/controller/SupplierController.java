package com.faos.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.faos.exception.InvalidEntityException;
import com.faos.model.Supplier;
import com.faos.service.EmailService;
import com.faos.service.PdfGeneratorService;
import com.faos.service.SupplierService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class SupplierController {

	    @Autowired
	    private SupplierService service;
	    
	    @Autowired
	    private EmailService emailService;
	    
	    @Autowired
	    private PdfGeneratorService pdfGeneratorService;

	    @GetMapping("/display")
	    public String display() {
	        return "Supplier Management System is Running!";
	    }

	    //add supplier details
	    @PostMapping("/addSupplier")
	    public Supplier addSupplier(@RequestBody @Validated Supplier supplier) {
	        supplier.setActive(true); // Default to active
	        return service.addSupplier(supplier);
	    }


	    //get all suppliers
	    @GetMapping("/suppliers")
	    public ResponseEntity<List<Supplier>> getAllSuppliers(Model model) {
	        return new ResponseEntity<>(service.getAllSuppliers(),HttpStatus.OK);
	    }
	 
	
	    // get all active suppliers
	    @GetMapping("/activeSupplier")
	    public List<Supplier> getActiveSuppliers() {
	        return service.getActiveSuppliers();
	    }
	    
	    // get all inactive suppliers
	    @GetMapping("/inactiveSupplier")
	    public List<Supplier> getInActiveSuppliers() {
	        return service.getInActiveSuppliers();
	    }

	
	    // get supplier by id
	    @GetMapping("/supplier/{id}")
	    public Supplier getSupplierById(@PathVariable String id) throws InvalidEntityException {
	    	//session.persist(supplier);
	        return service.getSupplierById(id);
	    }
	    
	    //update supplier
	    @PutMapping("/supplier/{id}")
	    public Supplier updateSupplier(@PathVariable String id, @RequestBody @Validated Supplier updatedSupplier) throws InvalidEntityException {
	        return service.updateSupplier(id,updatedSupplier);
	    }
	    
	    //deactivate supplier
	    @PutMapping("/deactivateSupplier/{id}")
		public ResponseEntity<String> deactivateSupplier(@PathVariable String id) throws InvalidEntityException {
	    	boolean isDeactivated = service.deactivateSupplier(id);
			if (isDeactivated) return ResponseEntity.ok("Supplier deactivated and admin notified.");
	        else return ResponseEntity.status(400).body("Failed to deactivate supplier.");
	        
		}
		
	    //reactivate supplier
		@PutMapping("/reactivateSupplier/{id}")
		public Supplier reactivateSupplier(@PathVariable String id) throws InvalidEntityException {
			return service.reactivateSupplier(id);
		}
		
		//download pdf
		@GetMapping("/downloadActiveSuppliersPdf")
	    public void downloadActiveSuppliersPdf(HttpServletResponse response) throws IOException {
	        response.setContentType("application/pdf");
	        String headerValue = "attachment; filename=active_suppliers.pdf";
	        response.setHeader("Content-Disposition", headerValue);

	        List<Supplier> activeSuppliers = service.getActiveSuppliers();
	        pdfGeneratorService.generateSupplierPdf(activeSuppliers, response);
	    }
	    
	    
	

	}



