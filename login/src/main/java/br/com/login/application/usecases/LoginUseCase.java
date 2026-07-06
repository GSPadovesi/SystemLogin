package br.com.login.application.usecases;

import br.com.login.application.commands.LoginCommand;
import br.com.login.application.gateways.AuthGateway;
import br.com.login.application.gateways.UserGateway;
import br.com.login.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class LoginUseCase {
    private final UserGateway userGateway;
    private final AuthGateway authGateway;

    public LoginUseCase(UserGateway userGateway, AuthGateway authGateway){
        this.userGateway = userGateway;
        this.authGateway = authGateway;
    }

    public String execute(LoginCommand command){
        User user = userGateway.findUserByEmail(command.email()).orElseThrow(() -> new UsernameNotFoundException("Usuário invalido"));

        boolean passwordValid = authGateway.passwordMatches(command.password(), user.getPassword());

        if(!passwordValid) throw new RuntimeException("Senha invalida");

        String newToken = authGateway.generateToken(user);
        return newToken;
    }
}
