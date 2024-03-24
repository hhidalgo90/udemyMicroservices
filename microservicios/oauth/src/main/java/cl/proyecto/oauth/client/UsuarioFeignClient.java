package cl.proyecto.oauth.client;

import cl.proyecto.usuarios.commons.models.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * Clase cliente http para consumir el ms usuarios usando apirest.
 */
@FeignClient(name = "microservicio-usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/usuarios/search/buscar-username")
    public Usuario findByUsername(@RequestParam String username);

    @PutMapping("/usuarios/{id}")
    public Usuario update (@RequestBody Usuario usuario, @PathVariable Long id);
}
