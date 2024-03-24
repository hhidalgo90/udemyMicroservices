package cl.proyecto.microservicioitems.models.dto;

import cl.proyecto.commons.models.entity.Producto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Producto producto;
    private Integer cantidad;

    public Double getTotal(){
        return this.producto.getPrecio() * this.cantidad.doubleValue();
    }
}
