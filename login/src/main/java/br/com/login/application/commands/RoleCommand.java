package br.com.login.application.commands;

import br.com.login.domain.UserRole;

import java.util.UUID;

public record RoleCommand(UserRole role, UUID id) {
}
