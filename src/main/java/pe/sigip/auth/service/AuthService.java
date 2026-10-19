package pe.sigip.auth.service;

import pe.sigip.auth.dto.LoginRequest;
import pe.sigip.auth.dto.RegistroRequest;
import pe.sigip.auth.model.Rol;
import pe.sigip.auth.model.Usuario;
import pe.sigip.auth.repository.RolRepository;
import pe.sigip.auth.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
                       PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Map<String, Object> registrar(RegistroRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }

        Rol rolCliente = rolRepository.findByNombre("CLIENTE")
                .orElseGet(() -> rolRepository.save(new Rol("CLIENTE")));

        Usuario usuario = new Usuario();
        usuario.setNombres(request.nombres());
        usuario.setCorreo(request.correo());
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setRol(rolCliente);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);

        return Map.of(
                "mensaje", "Usuario registrado correctamente",
                "id", usuario.getId(),
                "username", usuario.getUsername(),
                "rol", rolCliente.getNombre()
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Usuario o contraseña incorrectos"));

        if (!usuario.isActivo() || !passwordEncoder.matches(request.password(), usuario.getPassword())) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        return Map.of(
                "mensaje", "Inicio de sesión correcto",
                "id", usuario.getId(),
                "username", usuario.getUsername(),
                "nombres", usuario.getNombres(),
                "rol", usuario.getRol().getNombre()
        );
    }
}