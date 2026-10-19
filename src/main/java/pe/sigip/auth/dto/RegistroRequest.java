package pe.sigip.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistroRequest(
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Email @Size(max = 120) String correo,
        @NotBlank @Size(min = 4, max = 40) String username,
        @NotBlank @Size(min = 8, max = 72) String password
) {}