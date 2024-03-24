package cl.proyecto.microservicioitems.clientes;

import cl.proyecto.commons.models.entity.Producto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "microservicio-productos")
public interface ProductoClienteRest {

    @GetMapping("api/producto")
    public List<Producto> obtenerProductos();

    @GetMapping("api/producto/{id}")
    public Producto findById(@PathVariable Long id);

    @PostMapping("api/producto")
    public Producto guardar(@RequestBody Producto producto);

    @PutMapping("api/producto/{id}")
    public Producto editar(@RequestBody Producto producto, @PathVariable Long id);

    @DeleteMapping("api/producto/{id}")
    public void delete(@PathVariable Long id);
}
