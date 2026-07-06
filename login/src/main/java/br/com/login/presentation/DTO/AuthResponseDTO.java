package br.com.login.presentation.DTO;
import jakarta.validation.constraints.NotBlank;

public record AuthResponseDTO(
        @NotBlank(message = "O token e obrigatorio")
        String token

) {
}
