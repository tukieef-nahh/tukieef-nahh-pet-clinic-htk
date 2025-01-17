package com.example.PetClinicBE.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.PetClinicBE.entity.Veterinarian;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, Long> {
	boolean existsByPhone(String phone);
    Optional<Veterinarian> findByPhone(String phone);
    
    @Query("SELECT v FROM Veterinarian v " +
 	       "WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
 	       "OR LOWER(v.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
 	       "OR LOWER(v.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))" +
 	       "OR LOWER(v.specialization) LIKE LOWER(CONCAT('%', :keyword, '%'))")
     Page<Veterinarian> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

     Page<Veterinarian> findAll(Pageable pageable);
}
