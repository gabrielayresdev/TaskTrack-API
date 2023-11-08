package dev.gabrielayres.Todolist.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.gabrielayres.Todolist.users.UserModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Value must bem from factory annotation
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UserModel user, boolean remember) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("auth-api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(genExpirationDate(remember))
                    .sign(algorithm);
            return token;
        }   catch(JWTCreationException exception) {
            throw new RuntimeException("Error while generating token", exception);
        }
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        try {
            return JWT.require(algorithm)
                    .withIssuer("auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch(JWTVerificationException jwtVerificationException) {
            return "Token JWT inválido ou expirado!";
        }
    }

    private Instant genExpirationDate(boolean remember) {
        return LocalDateTime.now().plusHours(remember ? 168 : 3).toInstant(ZoneOffset.of("-03:00"));
    }
}
