package com.example.PetClinicBE.configuration;

import java.text.ParseException;
import java.util.Objects;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.example.PetClinicBE.dto.request.IntrospectRequest;
import com.example.PetClinicBE.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;

@Component
public class CustomJwtDecoder implements JwtDecoder {

	@Value("${jwt.signerKey}")
	private String signerKey;
	
	@Autowired
	private AuthenticationService authnticationService;
	
	private NimbusJwtDecoder nimbusJwtDecoder = null;

	@Override
	public Jwt decode(String token) throws JwtException {
		try {
			var response = authnticationService.introspect(IntrospectRequest.builder().token(token)
					.build());
			
			if(!response.isValid())
				throw new JwtException("Token không hợp lệ.");
		} catch (JOSEException | ParseException e) {
			throw new JwtException(e.getMessage());
		}
		
		if(Objects.isNull(nimbusJwtDecoder)) {
			SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS256");
			nimbusJwtDecoder = NimbusJwtDecoder
					.withSecretKey(secretKeySpec)
					.macAlgorithm(MacAlgorithm.HS256)
					.build();
		}
		
		return nimbusJwtDecoder.decode(token);
	}
}
