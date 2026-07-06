package br.com.login.presentation.controllers;

import br.com.login.application.commands.LoginCommand;
import br.com.login.application.commands.RegisterCommand;
import br.com.login.application.usecases.LoginUseCase;
import br.com.login.application.usecases.RegisterUseCase;
import br.com.login.presentation.DTO.AuthResponseDTO;
import br.com.login.presentation.DTO.LoginRequestDTO;
import br.com.login.presentation.DTO.RegisterRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthContoller {
    private final RegisterUseCase registerUseCase;
    private final LoginUseCase loginUseCase;

    public AuthContoller(RegisterUseCase registerUseCase, LoginUseCase loginUseCase){
        this.registerUseCase = registerUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterRequestDTO requestDTO){
        RegisterCommand command = new RegisterCommand(
                requestDTO.username(),
                requestDTO.email().trim().toLowerCase(),
                requestDTO.password()
        );

        String token = registerUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO requestDTO){
        LoginCommand command = new LoginCommand(
                requestDTO.email().trim().toLowerCase(),
                requestDTO.password()
        );

        String token = loginUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDTO(token));
    }

}
