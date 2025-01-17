package com.example.PetClinicBE.validator;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.PetClinicBE.entity.InvalidatedToken;


public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String>{

}
