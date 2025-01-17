package com.example.PetClinicBE.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.PetClinicBE.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByPhone(String phone);
	
	Optional<User> findByEmail(String email);
	Optional<User> findByPhone(String phone);
	
	@Query("SELECT u FROM User u " +
		       "WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
		       "OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	    Page<User> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

	    Page<User> findAll(Pageable pageable);
}
