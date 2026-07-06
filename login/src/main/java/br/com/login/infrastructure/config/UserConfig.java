package br.com.login.infrastructure.config;

import br.com.login.application.gateways.UserGateway;
import br.com.login.application.usecases.ChangeRoleUserUseCase;
import br.com.login.infrastructure.gateways.UserPersistenceAdapter;
import br.com.login.infrastructure.persistence.UserEntityMapper;
import br.com.login.infrastructure.persistence.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    @Bean
    public ChangeRoleUserUseCase changeRoleUserUseCase(UserGateway userGateway){
        return new ChangeRoleUserUseCase(userGateway);
    }

    @Bean
    public UserGateway userGateway(
            UserRepository userRepository,
            UserEntityMapper userEntityMapper
    ) {
        return new UserPersistenceAdapter(userRepository, userEntityMapper);
    }
}
