package br.com.login.presentation.controllers;

import br.com.login.application.commands.RoleCommand;
import br.com.login.application.usecases.ChangeRoleUserUseCase;
import br.com.login.presentation.DTO.RoleResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class UserController {
    private final ChangeRoleUserUseCase changeRoleUserUseCase;

    public UserController(ChangeRoleUserUseCase changeRoleUserUseCase){
        this.changeRoleUserUseCase = changeRoleUserUseCase;
    }

    @GetMapping("/admin")
    public String executeAdmin(){
        return "ADMIN";
    }

    @GetMapping("/user")
    public String executeUser(){
        return "User";
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Boolean> changeAdmin(@PathVariable UUID id, @RequestBody @Valid RoleResponseDTO roleResponseDTO){
        RoleCommand command = new RoleCommand(
                roleResponseDTO.userRole(),
                id
        );

        changeRoleUserUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
