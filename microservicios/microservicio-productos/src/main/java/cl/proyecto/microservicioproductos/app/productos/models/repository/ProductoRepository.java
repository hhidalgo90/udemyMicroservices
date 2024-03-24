package cl.proyecto.microservicioproductos.app.productos.models.repository;

import cl.proyecto.commons.models.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
