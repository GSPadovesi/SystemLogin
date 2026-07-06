package br.com.login.infrastructure.gateways;

import br.com.login.application.gateways.AuthGateway;
import br.com.login.domain.User;
import br.com.login.infrastructure.exceptions.TokenGenerationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class AuthPersistenceAdapter implements AuthGateway {
    @Value("${jwt.secret.api}")
    private String secretKey;
    private final PasswordEncoder passwordEncoder;

    public AuthPersistenceAdapter(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String passwordHash(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public String generateToken(User user) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.create()
                    .withIssuer("authentication-api")
                    .withSubject(user.getEmail())
                    .withClaim("role", user.getRole().name())
                    .withClaim("userId", user.getId().toString())
                    .withExpiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                    .sign(algorithm);
        } catch (Exception e) {
            throw new TokenGenerationException("Erro ao renderizar e assinar o token JWT", e);
        }
    }

    @Override
    public boolean passwordMatches(String password, String hashPassword) {
        return passwordEncoder.matches(password, hashPassword);
    }
}
