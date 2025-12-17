package com.cotrafa.creditapproval.shared.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expiration-minutes}")
    private long accessTokenExpirationMinutes;

    @Value("${jwt.refresh-token.expiration-days}")
    private long refreshTokenExpirationDays;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    public String createAccessToken(UUID userId, UUID sessionId) {
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("id", userId.toString())
                .withClaim("sid", sessionId.toString())
                .withClaim("type", "ACCESS")
                .withIssuer("database-integration")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(accessTokenExpirationMinutes)))
                .sign(algorithm);
    }

    public String createRefreshToken(UUID userId, UUID sessionId) {
        return JWT.create()
                .withSubject(userId.toString())
                .withClaim("id", userId.toString())
                .withClaim("sid", sessionId.toString())
                .withClaim("type", "REFRESH")
                .withIssuer("database-integration")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(refreshTokenExpirationDays)))
                .sign(algorithm);
    }

    public boolean isValid(String jwt) {
        try {
            JWT.require(algorithm)
                    .build()
                    .verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    public UUID getUserId(String jwt) {
        String id = JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getSubject();

        return UUID.fromString(id);
    }

    public UUID getSessionId(String jwt) {
        String sessionId = JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getClaim("sid")
                .asString();

        return UUID.fromString(sessionId);
    }

    public String getTokenType(String jwt) {
        return JWT.require(algorithm)
                .build()
                .verify(jwt)
                .getClaim("type")
                .asString();
    }
}