package com.example.JavaSpringBoot.configuration;

import com.example.JavaSpringBoot.dto.request.IntrospectRequest;
import com.example.JavaSpringBoot.dto.respose.IntrospectResponse;
import com.example.JavaSpringBoot.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) {
        try {
            IntrospectResponse response = authenticationService.introspect(IntrospectRequest.builder()
                            .token(token)
                    .build());
            if(!response.isValid()) {
                throw new JwtException("Token invalid");
            }
        } catch (Exception e) {
            throw new JwtException(e.getMessage());
        }
        if(nimbusJwtDecoder==null) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }

}
