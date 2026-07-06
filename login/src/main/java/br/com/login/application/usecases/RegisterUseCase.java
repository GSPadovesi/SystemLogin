package br.com.login.application.usecases;

import br.com.login.application.commands.LoginCommand;
import br.com.login.application.commands.RegisterCommand;
import br.com.login.application.gateways.AuthGateway;
import br.com.login.application.gateways.UserGateway;
import br.com.login.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class RegisterUseCase {
    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public RegisterUseCase(UserGateway userGateway, AuthGateway authGateway){
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    public String execute(RegisterCommand command){
        if(userGateway.existingUserByEmail(command.email())) throw new RuntimeException("Esse email ja esta vinculado a outra conta");

        String hashPassword = authGateway.passwordHash(command.password());

        User user = User.create(
                command.username(),
                command.email(),
                hashPassword
        );

        User savedUser = userGateway.save(user);
        String newToken = authGateway.generateToken(savedUser);

        return newToken;
    }
}
