package com.faos.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;


@Entity
public class Supplier {
	@Id
	private String supplierID;
	
	@NotEmpty(message="name should not be empty")
	@Column(length=30)
	private String name;
	
	@Pattern(regexp = "[6-9][0-9]{9}", message = "Contact must be a 10-digit number starting with 6, 7, 8, or 9")
	private String contact;
	
	@NotEmpty(message="address should not be empty")
	@Column(length=50)
	private String address;
	
	@NotEmpty(message="email should not be empty")
	@Email(regexp = ".+[@].+[\\.].+", message="invalid email")
	private String email;
	
	private boolean active=true;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="supplier")
	@JsonIgnoreProperties("supplier")
	private List<Cylinder> cylinderList;

	public String getSupplierID() {
		return supplierID;
	}

	public void setSupplierID(String supplierID) {
		this.supplierID = supplierID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Cylinder> getCylinderList() {
		return cylinderList;
	}

	public void setCylinderList(List<Cylinder> cylinderList) {
		this.cylinderList = cylinderList;
	}
	
    
}
