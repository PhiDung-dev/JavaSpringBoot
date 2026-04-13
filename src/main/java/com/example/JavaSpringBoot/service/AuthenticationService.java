package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.AuthenticationRequest;
import com.example.JavaSpringBoot.dto.request.IntrospectRequest;
import com.example.JavaSpringBoot.dto.respose.AuthenticationResponse;
import com.example.JavaSpringBoot.dto.respose.IntrospectResponse;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    @NonFinal
    @Value("${jwt.secret-key}")
    protected String secretKey;
    PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) {
            throw new AppException(ErrorCode.AUTHENTICATED);
        }
        var token = generateToken2(user);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .authenticate(authenticated)
                .token(token)
                .build();
        return authenticationResponse;
    }

    private String generateToken1(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", builderScope(user.getRoles()))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            JWSSigner signer = new MACSigner(secretKey.getBytes());
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can not create token", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect1(IntrospectRequest request) {
        String token = request.getToken();
        JWSObject jwsObject = null;
        try {
            jwsObject = JWSObject.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JWSVerifier verifier = null;
        try {
            verifier = new MACVerifier(secretKey.getBytes());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        boolean verified = false;
        try {
            verified = jwsObject.verify(verifier);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> json = jwsObject.getPayload().toJSONObject();
        Date exp = new Date((Long) json.get("exp"));
        return IntrospectResponse.builder()
                .valid(verified && exp.after(new Date()))
                .build();
    }

    private String generateToken2(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .claim("scope", builderScope(user.getRoles()))
                .build();
        SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
        try {
            JWSSigner jwsSigner = new MACSigner(secretKey.getBytes());
            signedJWT.sign(jwsSigner);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("can not create token", e);
            throw new RuntimeException(e);
        }
    }

    public IntrospectResponse introspect2(IntrospectRequest request) {
        var token = request.getToken();
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        JWSVerifier verifier = null;
        try {
            verifier = new MACVerifier(secretKey.getBytes());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        Date expTime = null;
        try {
            expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        boolean verified = false;
        try {
            verified = signedJWT.verify(verifier);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return IntrospectResponse.builder()
                    .valid(verified && expTime.after(new Date()))
                    .build();

    }

    private String builderScope(Set<String> roles) {
        StringBuilder scope = new StringBuilder();
        roles.forEach(s->{
            scope.append(s).append(" ");
        });
        return scope.toString().trim();
    }

}
