package com.example.PetClinicBE.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.PetClinicBE.entity.enums.Role;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final String [] POST_PUBLIC_ENDPOINTS = {"/users", "/auth/token", "/auth/introspect", "/auth/logout", "/auth/token/veterinarians", "/medical_records", "/appointments"};
	private final String [] GET_PUBLIC_ENDPOINTS = {"/users", "/medical_records"};
	private final String [] PUT_PUBLIC_ENDPOINTS = {"/users", "/appointments",  "/medical_records"};
	@Autowired
	private CustomJwtDecoder customJwtDecoder;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.authorizeHttpRequests(
				request -> request.requestMatchers(HttpMethod.POST, POST_PUBLIC_ENDPOINTS).permitAll()
				.requestMatchers(HttpMethod.GET, GET_PUBLIC_ENDPOINTS).permitAll()
				.requestMatchers(HttpMethod.PUT, PUT_PUBLIC_ENDPOINTS).permitAll()
				
				.requestMatchers(HttpMethod.GET, "/veterinarians").hasRole(Role.VETERINARIAN.name())
				.requestMatchers(HttpMethod.PUT, "/veterinarians").hasRole(Role.VETERINARIAN.name())
				.requestMatchers(HttpMethod.GET, "/medical_records").hasRole(Role.VETERINARIAN.name())
				.requestMatchers(HttpMethod.POST, "/medical_records").hasRole(Role.VETERINARIAN.name())
				.requestMatchers(HttpMethod.PUT, "/medical_records").hasRole(Role.VETERINARIAN.name())
				.requestMatchers(HttpMethod.PUT, "/waiting-medical-records").hasRole(Role.VETERINARIAN.name())
				
				.requestMatchers(HttpMethod.GET, "/admin").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.PUT, "/admin").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.POST, "/admin").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.DELETE, "/admin").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.GET, "/veterinarians").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.PUT, "/veterinarians").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.GET, "/medical_records").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.POST, "/medical_records").hasRole(Role.ADMIN.name())
				.requestMatchers(HttpMethod.PUT, "/medical_records").hasRole(Role.ADMIN.name())
				.anyRequest().authenticated());
		
		httpSecurity.oauth2ResourceServer(oauth2 -> 
			oauth2.jwt(jwtConfigurer -> 
				jwtConfigurer.decoder(customJwtDecoder)
					.jwtAuthenticationConverter(jwtAuthenticationConverter()))
		);
		
		httpSecurity.csrf(AbstractHttpConfigurer::disable);
		
		return httpSecurity.build();
	}
	
	
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		
		corsConfiguration.addAllowedOrigin("http://127.0.0.1:5500/");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}
	
	@Bean
	JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
	
	@Bean
	PasswordEncoder passwordEncode() {
		return new BCryptPasswordEncoder(10);
	}
}