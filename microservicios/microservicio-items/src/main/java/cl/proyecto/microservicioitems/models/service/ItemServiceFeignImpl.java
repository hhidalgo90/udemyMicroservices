package cl.proyecto.microservicioitems.models.service;

import cl.proyecto.commons.models.entity.Producto;
import cl.proyecto.microservicioitems.clientes.ProductoClienteRest;
import cl.proyecto.microservicioitems.models.dto.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("feignService")
@Primary //indicamos que es la implementacion por defecto que debe usar spring
public class ItemServiceFeignImpl implements IItemService {

    @Autowired
    private ProductoClienteRest clienteRest;
    @Override
    public List<Item> findAll() {
        List<Producto> productos = clienteRest.obtenerProductos();
        return productos.stream().map(p -> new Item(p, 1)).toList();
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        Producto producto = clienteRest.findById(id);
        return new Item(producto, cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        return clienteRest.guardar(producto);
    }

    @Override
    public Producto edit(Producto producto, Long id) {
        return clienteRest.editar(producto,id);
    }

    @Override
    public void delete(Long id) {
    clienteRest.delete(id);
    }
}
