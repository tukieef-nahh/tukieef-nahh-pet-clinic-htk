package com.example.PetClinicBE.service;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.PetClinicBE.dto.request.VeterinarianRequest;
import com.example.PetClinicBE.entity.Veterinarian;
import com.example.PetClinicBE.entity.enums.Role;
import com.example.PetClinicBE.repository.VeterinarianRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VeterinarianService {

	@Autowired
	private VeterinarianRepository veterinarianRepository;
	PasswordEncoder passwordEncoder;
	
	public Veterinarian createVeterinarian(VeterinarianRequest request) {
		if(veterinarianRepository.existsByPhone(request.getPhone()))
			throw new RuntimeException("Số điện thoại đã được sử dụng.");
		
		Veterinarian user = new Veterinarian();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setSpecialization(request.getSpecialization());
		user.setPassword(passwordEncoder.encode(request.getPassword()));	
		HashSet<String> roles = new HashSet<>();
		roles.add(Role.VETERINARIAN.name());
		user.setRoles(roles);
		
		return veterinarianRepository.save(user);
	}

	public Page<Veterinarian> getAllVeterinarians(Pageable pageable) {
		return veterinarianRepository.findAll(pageable);
	}

	public void deleteVeterinarian(Long veterinarianId) {
		veterinarianRepository.deleteById(veterinarianId);
	}

	public Veterinarian getVeterinarianById(Long veterinarianId) {
		Optional<Veterinarian> veterinarian = veterinarianRepository.findById(veterinarianId);
		return veterinarian.orElse(null);
	}

	public Veterinarian putVeterinarian(Veterinarian veterinarian) {
	    Optional<Veterinarian> existingVeterinarianWithPhone = veterinarianRepository.findByPhone(veterinarian.getPhone());
	    if (existingVeterinarianWithPhone.isPresent() && !existingVeterinarianWithPhone.get().getId().equals(veterinarian.getId())) {
	        throw new IllegalArgumentException("Số điện thoại đã được sử dụng.");
	    }

	    Optional<Veterinarian> existingUser = veterinarianRepository.findById(veterinarian.getId());
	    if (existingUser.isPresent()) {
	    	Veterinarian currentUser = existingUser.get();
	    	veterinarian.setRoles(currentUser.getRoles());
	        
	        // Hash mật khẩu nếu mật khẩu thay đổi
	        if (!currentUser.getPassword().equals(veterinarian.getPassword())) {
	        	veterinarian.setPassword(passwordEncoder.encode(veterinarian.getPassword()));
            }
	        return veterinarianRepository.save(veterinarian);
	    }
	    
	    return null;
	}

	public Page<Veterinarian> searchVeterinarians(String keyword, Pageable pageable) {
		return veterinarianRepository.searchByKeyword(keyword, pageable);
	}

	public Optional<Veterinarian> getVeterinarianByPhone(String phone) {
		return veterinarianRepository.findByPhone(phone);
	}

}