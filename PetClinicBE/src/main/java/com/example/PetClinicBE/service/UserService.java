package com.example.PetClinicBE.service;

import org.springframework.stereotype.Service;

import com.example.PetClinicBE.dto.request.UserCreationRequest;
import com.example.PetClinicBE.entity.User;
import com.example.PetClinicBE.entity.enums.Role;
import com.example.PetClinicBE.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	@Autowired
	private UserRepository userRepository;
	PasswordEncoder passwordEncoder;
	
	public User createUser(UserCreationRequest request) {
		if(userRepository.existsByPhone(request.getPhone()))
			throw new RuntimeException("Số điện thoại đã được sử dụng.");
		
		User user = new User();
		
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPhone(request.getPhone());
		user.setAddress(request.getAddress());
		//user.setPassword(request.getPassword());	
		user.setPassword(passwordEncoder.encode(request.getPassword()));	
		HashSet<String> roles = new HashSet<>();
		roles.add(Role.USER.name());
		user.setRoles(roles);
		
		return userRepository.save(user);
	}
	
//	public List<User> getUsers(){
//		return userRepository.findAll();
//	}
	
	public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
	}
	
	public void deleteUser(Long userId) {
		userRepository.deleteById(userId);
	}
	
	public User getUser(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		return user.orElse(null);
	}
	
	public User putUser(User user) {	    
	    Optional<User> existingUserWithPhone = userRepository.findByPhone(user.getPhone());
	    if (existingUserWithPhone.isPresent() && !existingUserWithPhone.get().getId().equals(user.getId())) {
	        throw new IllegalArgumentException("Số điện thoại đã được sử dụng.");
	    }

	    Optional<User> existingUser = userRepository.findById(user.getId());
	    if (existingUser.isPresent()) {
	    	User currentUser = existingUser.get();
	    	user.setRoles(currentUser.getRoles());
	        
	        // Hash mật khẩu nếu mật khẩu thay đổi
	        if (!currentUser.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
	        return userRepository.save(user);
	    }
	    
	    return null;
	}
	
	public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchByKeyword(keyword, pageable);
    }
	
	public Optional<User> getUserByPhone(String phone) {
	    return userRepository.findByPhone(phone);
	}
}
