package com.faos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.faos.model.Bill;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

}
