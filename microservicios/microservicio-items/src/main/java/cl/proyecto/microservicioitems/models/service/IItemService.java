package cl.proyecto.microservicioitems.models.service;

import cl.proyecto.commons.models.entity.Producto;
import cl.proyecto.microservicioitems.models.dto.Item;

import java.util.List;

public interface IItemService {

    public List<Item> findAll();
    public Item findById(Long id, Integer cantidad);
    public Producto save(Producto producto);
    public Producto edit(Producto producto, Long id);
    public void delete(Long id);
}
