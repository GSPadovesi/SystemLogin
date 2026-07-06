package br.com.login.application.gateways;

import br.com.login.domain.User;
import br.com.login.domain.UserRole;

import java.util.Optional;
import java.util.UUID;

public interface UserGateway {
    User save(User user);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(UUID id);
    boolean existingUserByEmail(String email);
}
