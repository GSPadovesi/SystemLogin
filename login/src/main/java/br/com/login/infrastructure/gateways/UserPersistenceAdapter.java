package br.com.login.infrastructure.gateways;

import br.com.login.application.gateways.UserGateway;
import br.com.login.domain.User;
import br.com.login.domain.UserRole;
import br.com.login.infrastructure.persistence.UserEntity;
import br.com.login.infrastructure.persistence.UserEntityMapper;
import br.com.login.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.UUID;

public class UserPersistenceAdapter implements UserGateway {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    public UserPersistenceAdapter(UserRepository userRepository, UserEntityMapper userEntityMapper){
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public User save(User user) {
        UserEntity savedEntity = userRepository.save(userEntityMapper.fromDomain(user));
        return userEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email.trim().toLowerCase())
                .map(userEntityMapper::toDomain);

    }

    @Override
    public Optional<User> findUserById(UUID id){
        return userRepository.findById(id).map(userEntityMapper::toDomain);
    }

    @Override
    public boolean existingUserByEmail(String email) {
        return userRepository.existsByEmail(email.trim().toLowerCase());
    }
}
