package br.com.login.infrastructure.persistence;

import br.com.login.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public UserEntity fromDomain(User user){
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

    public User toDomain(UserEntity userEntity){
        return  User.restore(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRole()
        );
    }
}
