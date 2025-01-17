package com.example.PetClinicBE.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PetClinicBE.dto.request.VeterinarianRequest;
import com.example.PetClinicBE.entity.Veterinarian;
import com.example.PetClinicBE.service.VeterinarianService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@CrossOrigin
public class VeterinarianController {

	private VeterinarianService veterinarianService;
	
	//Lấy ds
	@GetMapping("admin/veterinarians")
	public ResponseEntity<Page<Veterinarian>> getAllVeterinarians(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "8") int size) {
	    Page<Veterinarian> veterinarians = veterinarianService.getAllVeterinarians(PageRequest.of(page, size));
	    return new ResponseEntity<>(veterinarians, HttpStatus.OK);
	}
		
	//Lấy tt veterinarian theo ID
    @GetMapping("/veterinarians/{id}")
    public ResponseEntity<?> getVeterinarianById(@PathVariable("id") Long id) {
        Optional<Veterinarian> existVeterinarian = Optional.ofNullable(veterinarianService.getVeterinarianById(id));
        if (existVeterinarian.isPresent()) {
            return new ResponseEntity<>(existVeterinarian.get(), HttpStatus.OK);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Veterinarian not found with ID: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
	    
    @PostMapping("admin/veterinarians")
	Veterinarian createUser(@RequestBody VeterinarianRequest request) {
		return veterinarianService.createVeterinarian(request);
	}

    //Xóa veterinarian theo ID
    @DeleteMapping("admin/veterinarians/{id}")
    public ResponseEntity<?> deleteVeterinarianById(@PathVariable("id") Long id) {
    	Optional<Veterinarian> veterinarian = Optional.ofNullable(veterinarianService.getVeterinarianById(id));
    	if (veterinarian.isPresent()) {
	        veterinarianService.deleteVeterinarian(id);
	        Map<String, String> successResponse = new HashMap<>();
	        successResponse.put("message", "Xóa bác sĩ thành công");
	        return new ResponseEntity<>(successResponse, HttpStatus.OK);
	    } else {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "Không tìm thấy bác sĩ với Id: " + id);
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }	
    }

    //Cập nhật tt veterinarian
    @PutMapping("/veterinarians/{id}")
    public ResponseEntity<?> updateVeterinarianById(@PathVariable("id") Long id, @Valid @RequestBody Veterinarian veterinarian, BindingResult bindingResult) {
    	if (bindingResult.hasErrors()) {
    		Map<String, String> errorResponse = new HashMap<>();
    		errorResponse.put("message", "Validation error");
    		errorResponse.put("details", bindingResult.getFieldErrors().stream()
    				.map(error -> error.getField() + ": " + error.getDefaultMessage())
                	.reduce((error1, error2) -> error1 + ", " + error2)
                	.orElse("Unknown validation error"));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Optional<Veterinarian> existingVeterinarian = Optional.ofNullable(veterinarianService.getVeterinarianById(id));
        if (existingVeterinarian.isPresent()) {
            if (!existingVeterinarian.get().getPhone().equals(veterinarian.getPhone())) {
                Optional<Veterinarian> veterinarianWithPhone = veterinarianService.getVeterinarianByPhone(veterinarian.getPhone());
                if (veterinarianWithPhone.isPresent()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Số điện thoại đã được sử dụng");
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }
            }

            veterinarian.setId(id);
            try {
                Veterinarian updatedVeterinarian = veterinarianService.putVeterinarian(veterinarian);
                return new ResponseEntity<>(updatedVeterinarian, HttpStatus.OK);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Lỗi cập nhật bác sĩ");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Không tìm thấy bác sĩ với ID: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    // Tìm kiếm veterinarian theo từ khóa trong tất cả các trường
    @GetMapping("admin/veterinarians/search")
    public ResponseEntity<Page<Veterinarian>> searchVeterinarians(
	    @RequestParam String keyword,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "8") int size) {
	    Page<Veterinarian> veterinarians = veterinarianService.searchVeterinarians(keyword, PageRequest.of(page, size));
	    return new ResponseEntity<>(veterinarians, HttpStatus.OK);
	}
}