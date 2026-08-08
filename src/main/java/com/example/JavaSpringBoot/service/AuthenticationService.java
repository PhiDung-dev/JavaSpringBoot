package com.example.JavaSpringBoot.service;

import com.example.JavaSpringBoot.dto.request.AuthenticationRequest;
import com.example.JavaSpringBoot.dto.request.IntrospectRequest;
import com.example.JavaSpringBoot.dto.request.LogoutRequest;
import com.example.JavaSpringBoot.dto.request.RefreshRequest;
import com.example.JavaSpringBoot.dto.respose.AuthenticationResponse;
import com.example.JavaSpringBoot.dto.respose.IntrospectResponse;
import com.example.JavaSpringBoot.entity.InvalidatedToken;
import com.example.JavaSpringBoot.entity.User;
import com.example.JavaSpringBoot.exception.AppException;
import com.example.JavaSpringBoot.exception.ErrorCode;
import com.example.JavaSpringBoot.repository.InvalidatedTokenRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.secret-key}")
    protected String secretKey;

    @NonFinal
    @Value("${jwt.access-token-expiration}")
    protected long accessTokenExpiration;

    @NonFinal
    @Value("${jwt.refresh-token-expiration}")
    protected long refreshTokenExpiration;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if(!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        var token = generateToken(user);
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .authenticate(authenticated)
                .token(token)
                .build();
        return authenticationResponse;
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid;
        try {
            SignedJWT signedJWT = verifyToken(token, false);
            isValid = true;
        } catch (Exception e) {
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid)
                    .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            SignedJWT signedJWT = verifyToken(request.getToken(), true);
            String jti = signedJWT.getJWTClaimsSet().getJWTID();
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            invalidatedTokenRepository.save(new InvalidatedToken(jti, expiryTime));
        } catch (AppException e) {
            log.info("Token already expired");
        }

    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        SignedJWT signedJWT = verifyToken(request.getToken(), true);
        String jti = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        invalidatedTokenRepository.save(new InvalidatedToken(jti, expiryTime));
        User user = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject()).orElseThrow(()->new AppException(ErrorCode.UNAUTHENTICATED));
        String token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticate(true)
                .build();
    }

    private String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("service")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(accessTokenExpiration, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", builderScope(user))
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

    private String builderScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        user.getRoles().forEach(role -> {
            stringJoiner.add("ROLE_"+role.getName());
            if(!role.getPermissions().isEmpty()) {
                role.getPermissions().forEach(permission -> {
                    stringJoiner.add(permission.getName());
                });
            }
        });
        return stringJoiner.toString();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey.getBytes());
        boolean verified = signedJWT.verify(verifier);
        Date expTime = null;
        if(!isRefresh) {
            expTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        }
        else {
            expTime = new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(refreshTokenExpiration, ChronoUnit.SECONDS).toEpochMilli());
        }
        if(!(verified && expTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException((ErrorCode.UNAUTHENTICATED));
        }
        return signedJWT;
    }

//    public IntrospectResponse introspect1(IntrospectRequest request) {
//        String token = request.getToken();
//        JWSObject jwsObject = null;
//        try {
//            jwsObject = JWSObject.parse(token);
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
//        JWSVerifier verifier = null;
//        boolean verified = false;
//        try {
//            verifier = new MACVerifier(secretKey.getBytes());
//            verified = jwsObject.verify(verifier);
//        } catch (JOSEException e) {
//            throw new RuntimeException(e);
//        }
//        Map<String, Object> json = jwsObject.getPayload().toJSONObject();
//        Date exp = new Date((Long) json.get("exp"));
//        return IntrospectResponse.builder()
//                .valid(verified && exp.after(new Date()))
//                .build();
//    }

//    private String generateToken1(User user) {
//        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
//        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
//                .subject(user.getUsername())
//                .issuer("service")
//                .issueTime(new Date())
//                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
//                .claim("scope", builderScope(user.getRoles()))
//                .build();
//        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
//        JWSObject jwsObject = new JWSObject(header, payload);
//        try {
//            JWSSigner signer = new MACSigner(secretKey.getBytes());
//            jwsObject.sign(signer);
//            return jwsObject.serialize();
//        } catch (JOSEException e) {
//            log.error("Can not create token", e);
//            throw new RuntimeException(e);
//        }
//    }

}
