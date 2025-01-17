package com.example.PetClinicBE.dto.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
	private String name;
    private String phone;
    private String email;
    private String address;
    private String password;
    Set<String> roles;
}
