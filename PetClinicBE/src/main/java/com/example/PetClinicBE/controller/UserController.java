package com.example.PetClinicBE.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.PetClinicBE.dto.request.UserCreationRequest;
import com.example.PetClinicBE.entity.User;
import com.example.PetClinicBE.entity.enums.Role;
import com.example.PetClinicBE.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

@AllArgsConstructor
@RestController
@CrossOrigin
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/users")
	User createUser(@RequestBody UserCreationRequest request) {
		return userService.createUser(request);
	}
	
	@GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        Optional<User> existUser = Optional.ofNullable(userService.getUser(id));
        if (existUser.isPresent()) {
            return new ResponseEntity<>(existUser.get(), HttpStatus.OK);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "User not found with ID: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
	
	@GetMapping("/users/phone/{phone}")
	public ResponseEntity<?> getUserByPhone(@PathVariable("phone") String phone) {
	    Optional<User> existUser = userService.getUserByPhone(phone);
	    if (existUser.isPresent()) {
	        return new ResponseEntity<>(existUser.get(), HttpStatus.OK);
	    } else {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "User not found with Phone: " + phone);
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }
	}

	//Lấy ds
    @GetMapping("/admin/users")
	public ResponseEntity<Page<User>> getAllUsers(
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "8") int size) {
	    Page<User> users = userService.getAllUsers(PageRequest.of(page, size));
	    return new ResponseEntity<>(users, HttpStatus.OK);
	}
    
    @DeleteMapping("/admin/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
    	Optional<User> user = Optional.ofNullable(userService.getUser(id));
    	if (user.isPresent()) {
    		var roles = new HashSet<String>();
    		roles.add(Role.ADMIN.name());
	        if (user.get().getRoles() == User.builder().roles(roles)) {
	            Map<String, String> errorResponse = new HashMap<>();
	            errorResponse.put("message", "Không thể xóa tài khoản của ADMIN");
	            return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	        }

	        userService.deleteUser(id);
	        Map<String, String> successResponse = new HashMap<>();
	        successResponse.put("message", "Xóa người dùng thành công");
	        return new ResponseEntity<>(successResponse, HttpStatus.OK);
	    } else {
	        Map<String, String> errorResponse = new HashMap<>();
	        errorResponse.put("message", "Không tìm thấy người dùng với Id: " + id);
	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	    }	
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUserById(@PathVariable("id") Long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Validation error");
            errorResponse.put("details", bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .reduce((error1, error2) -> error1 + ", " + error2)
                    .orElse("Unknown validation error"));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Optional<User> existingUser = Optional.ofNullable(userService.getUser(id));
        if (existingUser.isPresent()) {
            if (!existingUser.get().getPhone().equals(user.getPhone())) {
                Optional<User> userWithPhone = userService.getUserByPhone(user.getPhone());
                if (userWithPhone.isPresent()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("message", "Số điện thoại đã được sử dụng");
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }
            }

            user.setId(id);
            try {
            	User updatedUser = userService.putUser(user);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } catch (Exception e) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Lỗi cập nhật người dùng");
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Không tìm thấy người dùng với ID: " + id);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/admin/users/search")
    public ResponseEntity<Page<User>> searchUsers(
	    @RequestParam String keyword,
	    @RequestParam(defaultValue = "0") int page,
	    @RequestParam(defaultValue = "8") int size) {
	    Page<User> users = userService.searchUsers(keyword, PageRequest.of(page, size));
	    return new ResponseEntity<>(users, HttpStatus.OK);
	}
}
