package com.faos.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.faos.model.Supplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Controller
@Configuration
public class ClientController {
	
	@Autowired
    public RestTemplate getRestTemplate;

    private final String backendUrl = "http://localhost:8080";

    @GetMapping("/supplier/index")
    public String home() {
        return "supplierindex";
    }

    //view supplier
    @GetMapping("/suppliers")
    public String viewSuppliers(Model model) {
        ResponseEntity<Supplier[]> response = getRestTemplate.getForEntity(backendUrl + "/suppliers", Supplier[].class);
        List<Supplier> suppliers = Arrays.asList(response.getBody());
        model.addAttribute("suppliers", suppliers);
        return "viewsuppliers";
    }

    // add supplier form
    @GetMapping("/addsupplier")
    public String addSupplierForm(Model model) {
    	System.out.println("add supplier form");
        model.addAttribute("supplier", new Supplier());
        return "addsupplier";
    }

    //add supplier details to backend
    @PostMapping("/suppliers/add")
    public String addSupplier(@ModelAttribute Supplier supplier,BindingResult bindingResult, Model model) {
    
	    if (bindingResult.hasErrors()) {
	    	System.out.print("error occured");
	    	System.out.println("Validation errors: " + bindingResult.getAllErrors());
	        
	        model.addAttribute("supplier", supplier); // Return back to the form with error messages
	        return "addsupplier";
	    }
        
        try {
        	ResponseEntity<Supplier> response = getRestTemplate.postForEntity(backendUrl + "/addSupplier", supplier, Supplier.class);
        	Supplier suppObj = response.getBody();
        	model.addAttribute("message", "Supplier details added successfully!");
            model.addAttribute("supplier", suppObj); // Reset the form fields
            return "success";


	    } catch (HttpClientErrorException e) {
	        // Handle backend error and display error message
	        //handleBackendError(e, model);
	        Map<String, String> errors=null;;

			try {
				errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});

			} catch(JsonMappingException e1){	e1.printStackTrace();

			} catch (JsonProcessingException e1) {	e1.printStackTrace();	}
		// Map backend errors to BindingResult				

		for(Map.Entry<String, String> entryset : errors.entrySet()) {
			String field = entryset.getKey();
			String errorMsg = entryset.getValue();							
			bindingResult.rejectValue(field,"",errorMsg);
		}
	    }
        return "addsupplier";
		
    }


    @GetMapping("/suppliers/edit/{id}")
    public String editSupplierForm(@PathVariable String id, Model model) {
        // Fetch supplier details from backend
    	System.out.println(id);
        ResponseEntity<Supplier> response = getRestTemplate.getForEntity(backendUrl + "/supplier/" + id, Supplier.class);
        Supplier supplier = response.getBody();
        model.addAttribute("supplier", supplier);
        return "editsupplier"; // Map to editsupplier.html
    }

    @PostMapping("/suppliers/update")
    public String updateSupplier(@ModelAttribute("supplier") Supplier supplier,BindingResult bindingResult, Model model) {
    	if (bindingResult.hasErrors()) {
	    	System.out.print("error occured");
	    	System.out.println("Validation errors: " + bindingResult.getAllErrors());
	        
	        model.addAttribute("supplier", supplier); // Return back to the form with error messages
	        return "editsupplier";
	    }
        
    	try {
    		getRestTemplate.put(backendUrl + "/supplier/" + supplier.getSupplierID(), supplier);
    		return "redirect:/suppliers";
    		
    	}catch (HttpClientErrorException e) {
	        // Handle backend error and display error message
	        //handleBackendError(e, model);
	        Map<String, String> errors=null;;

			try {
				errors = new ObjectMapper().readValue(e.getResponseBodyAsString(), new TypeReference<Map<String, String>>() {});

			} catch(JsonMappingException e1){	e1.printStackTrace();

			} catch (JsonProcessingException e1) {	e1.printStackTrace();	}
		// Map backend errors to BindingResult				

		for(Map.Entry<String, String> entryset : errors.entrySet()) {
			String field = entryset.getKey();
			String errorMsg = entryset.getValue();							
			bindingResult.rejectValue(field,"",errorMsg);
		}
	    }
    	return "editsupplier";
    }

    @GetMapping("/deactivateForm")
    public String deactivateForm(@RequestParam(required=false) String id, Model model) {
    	if(id!=null) {
			ResponseEntity<Supplier> response = getRestTemplate.getForEntity(backendUrl +"/supplier/"+id, Supplier.class);
			if(response.hasBody()) {
				model.addAttribute("supplier",response.getBody());
				model.addAttribute("message", "Supplier deactivated successfully!");
				return "success";
			}
		}
		return "deactivate";
    	
    }
    
    @PostMapping("/deactivate")
	public String deactivateSupplier(@RequestParam String id, Model model) {
		try {
			getRestTemplate.put(backendUrl +"/deactivateSupplier/"+id, Supplier.class);
			//model.addAttribute("message","Supplier deactivated successfully");
			return "redirect:/suppliers";
		}catch(HttpClientErrorException e) {
			handleBackendError(e,model);
			return "deactivate";
		}
		
	}


    private void handleBackendError(HttpClientErrorException e, Model model) {
		try {
			String responseBody = e.getResponseBodyAsString();
			System.out.println("response body: "+responseBody);
			
			ObjectMapper objMapper = new ObjectMapper();
			Map<String,String> errorResponse = objMapper.readValue(responseBody, new TypeReference<Map<String,String>>() {});
			
			model.addAttribute("errorMessage", errorResponse.get("message"));
			System.out.println("Error Message from Backend: " + errorResponse.get("message"));
		}catch(JsonProcessingException ex) {
			model.addAttribute("errorMessage","Unable to process error reponse from backend");
		}
	}
}
