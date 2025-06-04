package com.faos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faos.model.Login;

public interface LoginRepository extends JpaRepository<Login, String> {
	Login findByUserId(String userId);
}
 
