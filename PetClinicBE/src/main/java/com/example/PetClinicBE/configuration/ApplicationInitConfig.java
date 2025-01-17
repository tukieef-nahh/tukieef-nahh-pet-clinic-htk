package com.example.PetClinicBE.configuration;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.PetClinicBE.entity.User;
import com.example.PetClinicBE.entity.enums.Role;
import com.example.PetClinicBE.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean
	ApplicationRunner applicationRunner(UserRepository userRepository) {
		return args -> {
			if (userRepository.findByPhone("0818166972").isEmpty()) {
				var roles = new HashSet<String>();

				roles.add(Role.ADMIN.name());

				User user = User.builder()
						.name("Admin")
						.phone("0818166972")
						.password(passwordEncoder.encode("Admin@1234")).roles(roles)
						.roles(roles)
						.address("Đông Hòa")
						.email("admin@gmail.com")
						.build();

				userRepository.save(user);
			}
		};
	}
}
