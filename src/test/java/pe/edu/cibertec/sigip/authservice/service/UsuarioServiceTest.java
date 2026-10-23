package pe.edu.cibertec.sigip.authservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.cibertec.sigip.authservice.dto.UsuarioAdminRequest;
import pe.edu.cibertec.sigip.authservice.model.Rol;
import pe.edu.cibertec.sigip.authservice.model.Usuario;
import pe.edu.cibertec.sigip.authservice.repository.RolRepository;
import pe.edu.cibertec.sigip.authservice.repository.UsuarioRepository;
import pe.edu.cibertec.sigip.common.error.ReglaNegocioException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/** Pruebas unitarias de la administración de usuarios internos (sin base de datos). */
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioAdminRequest request;

    @BeforeEach
    void setUp() {
        request = new UsuarioAdminRequest(
                "ventas01",
                "ventas01@sigip.test",
                "Ventas123",
                "Ana",
                "Pérez",
                true,
                Set.of("VENTAS")
        );
    }

    @Test
    void registraUsuarioInternoConPasswordCifradaYRolVentas() {
        when(rolRepository.findByNomrol("VENTAS")).thenReturn(Optional.of(new Rol(2, "VENTAS")));
        when(passwordEncoder.encode("Ventas123")).thenReturn("$2a$hash");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        var respuesta = usuarioService.registrarPorAdmin(request);

        assertThat(respuesta.roles()).containsExactly("VENTAS");
        verify(usuarioRepository).save(org.mockito.ArgumentMatchers.argThat(u ->
                u.getPassword().equals("$2a$hash") && Boolean.TRUE.equals(u.getActivo())));
    }

    @Test
    void rechazaUsuarioDuplicado() {
        when(usuarioRepository.existsByNomusuario("ventas01")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrarPorAdmin(request))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("usuario ya está registrado");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void rechazaCorreoDuplicado() {
        when(usuarioRepository.existsByEmail("ventas01@sigip.test")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.registrarPorAdmin(request))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("correo");
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void rechazaRolClienteEnElSistemaInterno() {
        UsuarioAdminRequest requestCliente = new UsuarioAdminRequest(
                "cliente01",
                "cliente01@sigip.test",
                "Cliente123",
                "Luis",
                "Rojas",
                true,
                Set.of("CLIENTE")
        );

        assertThatThrownBy(() -> usuarioService.registrarPorAdmin(requestCliente))
                .isInstanceOf(ReglaNegocioException.class)
                .hasMessageContaining("Rol no permitido");
        verify(usuarioRepository, never()).save(any());
    }
}
