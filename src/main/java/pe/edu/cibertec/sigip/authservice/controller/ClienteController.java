package pe.edu.cibertec.sigip.authservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.sigip.authservice.dto.ClienteRequest;
import pe.edu.cibertec.sigip.authservice.dto.UsuarioResponse;
import pe.edu.cibertec.sigip.authservice.service.UsuarioService;

import java.util.List;

/** Maestro de clientes utilizado por el área comercial. */
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','VENTAS')")
public class ClienteController {

    private final UsuarioService service;

    @GetMapping
    public List<UsuarioResponse> listar() { return service.listarClientes(); }

    @PostMapping
    public ResponseEntity<UsuarioResponse> registrar(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrarClienteInterno(request));
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable Integer id, @Valid @RequestBody ClienteRequest request) {
        return service.actualizarCliente(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
