package br.com.login.application.commands;

import br.com.login.domain.UserRole;

public record UserCommand(
        String id,
        String username,
        String email,
        String password,
        UserRole role
) {
}
