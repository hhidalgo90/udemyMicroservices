package cl.proyecto.microservicioitems.controllers;

import cl.proyecto.commons.models.entity.Producto;
import cl.proyecto.microservicioitems.models.dto.Item;
import cl.proyecto.microservicioitems.models.service.IItemService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("api/item")
@Getter
@Setter
@RefreshScope
public class ItemController {
    @Autowired
    @Qualifier("feignService")
    private IItemService itemService;

    @Value("${textoProperties}")
    private String textoProperties;

    @Autowired
    private CircuitBreakerFactory cbFactory;

    @Autowired
    private Environment environment;

    @GetMapping
    public List<Item> findAll(@RequestParam(name = "nombre", required = false) String nombre, @RequestHeader(name = "token-request", required = false) String token){
        System.out.println("RequestParam nombre: " + nombre + " RequestHeader token-request " + token);
        return itemService.findAll();
    }

    /**
     * Circuit breaker con CircuitBreakerFactory
     * @param id
     * @param cantidad
     * @return
     */
    @GetMapping("/{id}")
    public Item findById(@PathVariable Long id, @PathParam("cantidad") Integer cantidad){
       // return itemService.findById(id, cantidad);

        //Implementacion de Circuit breaker con Resilience4J
        return (Item) cbFactory.create("items").run(() -> itemService.findById(id, cantidad), throwable -> metodoAlternativo());
    }

    /**
     * Circuit breaker con anotaciones
     * @param id
     * @param cantidad
     * @return
     */
    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo")
    @GetMapping("/V2/{id}")
    public Item findById2(@PathVariable Long id, @PathParam("cantidad") Integer cantidad){

        return itemService.findById(id, cantidad);
    }

    /**
     * Circuit breaker con TimeLimiter, permite implementar timeout
     * @param id
     * @param cantidad
     * @return
     */
    @CircuitBreaker(name = "items", fallbackMethod = "metodoAlternativo2")
    @TimeLimiter(name = "items")
    @GetMapping("/V3/{id}")
    public CompletableFuture<Item> findById3(@PathVariable Long id, @PathParam("cantidad") Integer cantidad){

        return CompletableFuture.supplyAsync(()-> itemService.findById(id, cantidad));
    }

    public Item metodoAlternativo(){
        Item item = new Item();
        item.setCantidad(1);
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto defecto");
        producto.setPrecio(500.00);
        item.setProducto(producto);
        return item;
    }

    public CompletableFuture<Item> metodoAlternativo2(){
        Item item = new Item();
        item.setCantidad(1);
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Producto defecto");
        producto.setPrecio(500.00);
        item.setProducto(producto);
        return CompletableFuture.supplyAsync(()-> item);
    }

    @GetMapping("/obtener-config")
    public ResponseEntity<?> obtenerConfiguracion(@Value("${server.port}") String puerto){
        Map<String, String> json = new HashMap<>();
        json.put("textoProperties" , this.textoProperties);
        json.put("puerto" , puerto);

        if(environment.getActiveProfiles().length > 0 && environment.getActiveProfiles()[0].equals("development")){
            json.put("autor.nombre" , environment.getProperty("conf.autor.nombre"));
            json.put("autor.email" , environment.getProperty("conf.autor.email"));
        }

        return new ResponseEntity<Map<String, String>>(json, HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Producto guardarProducto(@RequestBody Producto producto){
        return itemService.save(producto);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Producto editar(@RequestBody Producto producto, @PathVariable Long id){
        return itemService.edit(producto, id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id){
        itemService.delete(id);
    }


}
