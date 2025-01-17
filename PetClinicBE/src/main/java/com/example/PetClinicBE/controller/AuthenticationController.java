package com.example.PetClinicBE.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.PetClinicBE.dto.request.AuthenticationRequest;
import com.example.PetClinicBE.dto.request.IntrospectRequest;
import com.example.PetClinicBE.dto.request.LogoutRequest;
import com.example.PetClinicBE.dto.response.ApiResponse;
import com.example.PetClinicBE.dto.response.AuthenticationResponse;
import com.example.PetClinicBE.dto.response.IntrospectResponse;
import com.example.PetClinicBE.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
	
	AuthenticationService authenticationService;
	
	@PostMapping("/token")
	ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
		if (request.getPhone() == null || request.getPhone().isEmpty()) {
	        throw new RuntimeException("Số điện thoại không được để trống.");
	    }
	    if (request.getPassword() == null || request.getPassword().length() < 6) {
	        throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự.");
	    }
		
		var result = authenticationService.authenticate(request);
		return ApiResponse.<AuthenticationResponse>builder()
				.result(result)
				.build();
	}
	
	@PostMapping("/token/veterinarians")
    ApiResponse<AuthenticationResponse> authenticateVeterinarian(@RequestBody AuthenticationRequest request) {
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new RuntimeException("Số điện thoại không được để trống.");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new RuntimeException("Mật khẩu phải có ít nhất 6 ký tự.");
        }

        var result = authenticationService.authenticateVeterinarian(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result)
                .build();
    }
	
	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
		var result = authenticationService.introspect(request);
		return ApiResponse.<IntrospectResponse>builder()
				.result(result)
				.build();
	}
	
	@PostMapping("/introspect/veterinarians")
    ApiResponse<IntrospectResponse> introspectVeterinarian(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .build();
    }
	
	@PostMapping("/logout")
	ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
		authenticationService.logout(request);
		return ApiResponse.<Void>builder()
				.build();
	}
}
