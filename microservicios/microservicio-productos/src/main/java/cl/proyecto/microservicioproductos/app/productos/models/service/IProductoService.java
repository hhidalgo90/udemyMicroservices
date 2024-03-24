package cl.proyecto.microservicioproductos.app.productos.models.service;


import cl.proyecto.commons.models.entity.Producto;

import java.util.List;

public interface IProductoService {

    public List<Producto> findAll();

    public Producto findById(Long id);

    public Producto save(Producto producto);

    public void deleteById(Long id);

}
