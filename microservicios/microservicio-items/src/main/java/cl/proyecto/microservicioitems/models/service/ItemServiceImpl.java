package cl.proyecto.microservicioitems.models.service;

import cl.proyecto.commons.models.entity.Producto;
import cl.proyecto.microservicioitems.models.dto.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service("restTemplateImpl")
public class ItemServiceImpl implements IItemService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ruta.base.microservicio.producto}")
    private String BASE_PATH;
    @Override
    public List<Item> findAll() {
        List<Producto> productos = Arrays.asList(restTemplate.getForObject(BASE_PATH, Producto[].class));

        return productos.stream().map(p -> new Item(p, 1)).toList();
    }

    @Override
    public Item findById(Long id, Integer cantidad) {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("id", id.toString());

        Producto producto = restTemplate.getForObject(BASE_PATH + "{id}", Producto.class, parametros);
        return new Item(producto, cantidad);
    }

    @Override
    public Producto save(Producto producto) {
        log.info("Save method: " + BASE_PATH);
        HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
        return restTemplate.exchange(BASE_PATH, HttpMethod.POST, body, Producto.class).getBody();
    }

    @Override
    public Producto edit(Producto producto, Long id) {
        HttpEntity<Producto> body = new HttpEntity<Producto>(producto);
        Map<String, String> parametros = new HashMap<>();
        parametros.put("id", id.toString());

        ResponseEntity<Producto> respuesta = restTemplate.exchange(BASE_PATH + "{id}", HttpMethod.PUT, body, Producto.class, parametros);
        return respuesta.getBody();
    }

    @Override
    public void delete(Long id) {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("id", id.toString());

        restTemplate.delete(BASE_PATH + "{id}", parametros);
    }
}
