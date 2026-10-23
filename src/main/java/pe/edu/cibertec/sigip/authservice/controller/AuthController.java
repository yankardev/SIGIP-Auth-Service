package pe.edu.cibertec.sigip.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.sigip.authservice.dto.LoginRequest;
import pe.edu.cibertec.sigip.authservice.dto.LoginResponse;
import pe.edu.cibertec.sigip.authservice.dto.UsuarioResponse;
import pe.edu.cibertec.sigip.authservice.service.UsuarioService;
import pe.edu.cibertec.sigip.common.security.JwtService;

import java.util.List;

/** Autenticación de usuarios internos de SIGIP. */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.nomusuario(), request.password()));
        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String token = jwtService.generarToken(authentication.getName(), authorities);
        return ResponseEntity.ok(new LoginResponse(token, "Bearer",
                usuarioService.obtenerPorNombre(authentication.getName())));
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponse> me(Authentication authentication) {
        return ResponseEntity.ok(usuarioService.obtenerPorNombre(authentication.getName()));
    }

    @GetMapping("/authorities")
    public ResponseEntity<List<String>> authorities(Authentication authentication) {
        return ResponseEntity.ok(authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());
    }
}
