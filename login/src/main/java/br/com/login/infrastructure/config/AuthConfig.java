package br.com.login.infrastructure.config;

import br.com.login.application.gateways.AuthGateway;
import br.com.login.application.gateways.UserGateway;
import br.com.login.application.usecases.LoginUseCase;
import br.com.login.application.usecases.RegisterUseCase;
import br.com.login.infrastructure.gateways.AuthPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthConfig {
    @Bean
    RegisterUseCase registerUseCase(UserGateway userGateway, AuthGateway authGateway){
        return new RegisterUseCase(userGateway, authGateway);
    }

    @Bean
    LoginUseCase loginUseCase(UserGateway userGateway, AuthGateway authGateway){
        return new LoginUseCase(userGateway, authGateway);
    }

    @Bean
    AuthGateway authGateway(PasswordEncoder passwordEncoder){
        return new AuthPersistenceAdapter(passwordEncoder);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
