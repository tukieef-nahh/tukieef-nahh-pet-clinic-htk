package com.example.PetClinicBE.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.example.PetClinicBE.dto.request.AuthenticationRequest;
import com.example.PetClinicBE.dto.request.IntrospectRequest;
import com.example.PetClinicBE.dto.request.LogoutRequest;
import com.example.PetClinicBE.dto.response.AuthenticationResponse;
import com.example.PetClinicBE.dto.response.IntrospectResponse;
import com.example.PetClinicBE.entity.InvalidatedToken;
import com.example.PetClinicBE.entity.User;
import com.example.PetClinicBE.entity.Veterinarian;
import com.example.PetClinicBE.repository.UserRepository;
import com.example.PetClinicBE.repository.VeterinarianRepository;
import com.example.PetClinicBE.validator.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.JWSVerifier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    
    UserRepository userRepository;
    VeterinarianRepository veterinarianRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;
    
    // Phương thức introspect token (chung cho User và Veterinarian)
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean inValid = true;
        
        try {
            verifyToken(token);    
        } catch (RuntimeException e) {
            inValid = false;
        }
        
        return IntrospectResponse.builder()
                .valid(inValid)
                .build();
    }
    
    // Phương thức authenticate cho User
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Không được để trống số điện thoại.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Không được để trống password.");
        }
        
        var user = userRepository.findByPhone(request.getPhone()).orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
        
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        
        if(!authenticated)
            throw new RuntimeException("Thông tin đăng nhập không hợp lệ.");
        
        //Tao token cho User
        var token = generateTokenForUser(user);
        
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    
    // Phương thức authenticate cho Veterinarian
    public AuthenticationResponse authenticateVeterinarian(AuthenticationRequest request) {
        if (request.getPhone() == null || request.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Không được để trống số điện thoại.");
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Không được để trống password.");
        }
        
        var veterinarian = veterinarianRepository.findByPhone(request.getPhone()).orElseThrow(() -> new RuntimeException("Bác sĩ không tồn tại"));
        
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), veterinarian.getPassword());
        
        if(!authenticated)
            throw new RuntimeException("Thông tin đăng nhập không hợp lệ.");
        
        //Tao token cho Veterinarian
        var token = generateTokenForVeterinarian(veterinarian);
        
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    
    // Phương thức logout chung cho cả User và Veterinarian
    public void logout(LogoutRequest request) throws JOSEException, ParseException {
        var signToken = verifyToken(request.getToken());
        
        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();
        
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();
        
        invalidatedTokenRepository.save(invalidatedToken);
    }
    
    // Phương thức verifyToken (chung cho User và Veterinarian)
    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        
        SignedJWT signedJWT = SignedJWT.parse(token);
        
        //kiem tra het han token
        Date expityTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        
        var verified = signedJWT.verify(verifier);
        
        if(!(verified && expityTime.after(new Date())))
            throw new RuntimeException("Thông tin token không hợp lệ.");
        
        //tra ve false cho token da logout - het hieu luc
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new RuntimeException("Thông tin token không hợp lệ.");
        
        return signedJWT;
    }
    
    // Phương thức tạo token cho User
    private String generateTokenForUser(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getPhone())
                .issuer("petclinic.htk")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("role", buildScope(user))
                .build();
        
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        
        JWSObject jwsObject = new JWSObject(header, payload);
        
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }        
    }
    
    // Phương thức tạo token cho Veterinarian
    private String generateTokenForVeterinarian(Veterinarian veterinarian) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(veterinarian.getPhone())
                .issuer("petclinic.htk")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("role", buildScopeVeterinarian(veterinarian))
                .build();
        
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        
        JWSObject jwsObject = new JWSObject(header, payload);
        
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }        
    }
    
    // Phương thức xây dựng scope cho User
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(stringJoiner::add);
        
        return stringJoiner.toString();
    }
    
    // Phương thức xây dựng scope cho Veterinarian
    private String buildScopeVeterinarian(Veterinarian veterinarian) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(veterinarian.getRoles()))
            veterinarian.getRoles().forEach(stringJoiner::add);
        
        return stringJoiner.toString();
    }
}
