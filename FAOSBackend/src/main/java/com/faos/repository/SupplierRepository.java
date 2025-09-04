package com.faos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.faos.model.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, String>{
	
	@Query(value = "SELECT MAX(supplierid) FROM supplier", nativeQuery = true)
	String findMaxSupplierId();
	
	@Query(value="select * from Supplier where active =1;", nativeQuery=true)
	List<Supplier> findActiveSuppliers();
	
	@Query(value="select * from Supplier where active =0;", nativeQuery=true)
	List<Supplier> findInActiveSuppliers();
}
