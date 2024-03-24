package cl.proyecto.microservicioproductos.app.productos.controllers;

import cl.proyecto.commons.models.entity.Producto;
import cl.proyecto.microservicioproductos.app.productos.models.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/producto")
public class ProductoController {

    @Autowired
    private IProductoService service;
    @GetMapping
    public List<Producto> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Producto findById(@PathVariable Long id) throws InterruptedException {
        if(id == 5){
            System.out.println("id producto: " + id);
            Thread.sleep(10000);
        }
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto producto){
        return service.save(producto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id){
        service.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto modificar(@RequestBody Producto producto, @PathVariable Long id) {
        Producto productoExistente = service.findById(id);

        productoExistente.setNombre(producto.getNombre());
        productoExistente.setPrecio(producto.getPrecio());
        return service.save(productoExistente);
    }
}
