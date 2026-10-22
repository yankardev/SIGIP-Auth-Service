package pe.edu.cibertec.sigip.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.sigip.authservice.dto.UsuarioAdminRequest;
import pe.edu.cibertec.sigip.authservice.dto.UsuarioResponse;
import pe.edu.cibertec.sigip.authservice.service.UsuarioService;

import java.util.List;

/**
 * Usuarios de SIGIP.
 * ADMIN administra cuentas; VENTAS puede consultar clientes para registrar pedidos en mostrador.
 */
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public ResponseEntity<List<UsuarioResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioService.obtener(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody UsuarioAdminRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarPorAdmin(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Integer id,
                                                      @Valid @RequestBody UsuarioAdminRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
