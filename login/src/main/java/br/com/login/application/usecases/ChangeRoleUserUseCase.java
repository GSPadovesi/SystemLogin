package br.com.login.application.usecases;

import br.com.login.application.commands.RoleCommand;
import br.com.login.application.commands.UserCommand;
import br.com.login.application.gateways.UserGateway;
import br.com.login.domain.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public class ChangeRoleUserUseCase {
    private final UserGateway userGateway;

    public ChangeRoleUserUseCase(UserGateway userGateway){
        this.userGateway = userGateway;
    }

    public boolean execute(RoleCommand command){
        User user = userGateway.findUserById(command.id()).orElseThrow(() -> new UsernameNotFoundException("Usuário invalido"));
        if(user.getRole().equals(command.role())) throw new IllegalArgumentException("Erro ao alterar role");
        user.setRole(command.role());
        userGateway.save(user);
        return true;
    }
}
