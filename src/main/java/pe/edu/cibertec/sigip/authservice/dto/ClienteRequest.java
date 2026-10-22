package pe.edu.cibertec.sigip.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(
        @NotBlank @Size(min = 4, max = 50) String nomusuario,
        @NotBlank @Email @Size(max = 120) String email,
        @Size(max = 100) String password,
        @NotBlank @Size(max = 100) String nombres,
        @NotBlank @Size(max = 100) String apellidos,
        Boolean activo
) {
}
