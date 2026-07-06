package br.com.login.application.gateways;

import br.com.login.domain.User;

public interface AuthGateway {
    String passwordHash(String password);
    String generateToken(User user);
    boolean passwordMatches(String password, String hashPassword);
}
