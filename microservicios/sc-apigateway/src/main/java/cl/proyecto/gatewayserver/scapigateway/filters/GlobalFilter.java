package cl.proyecto.gatewayserver.scapigateway.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class GlobalFilter implements org.springframework.cloud.gateway.filter.GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(GlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Ejecutando PRE FILTER");

        exchange.getRequest().mutate().headers(header-> header.add("token", "1234567"));
        //ejecuta los filtros en cadena
        //antes del return es filtro pre
        //Mono permite crear un objeto reactivo que hace algo
        return chain.filter(exchange).then(Mono.fromRunnable(()-> {
            //Post filter
            logger.info("FILTRO POST");
            //hacemos algo con la respuesta, esto aplica para todos los request
            Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor -> {
                exchange.getResponse().getHeaders().add("token", valor);
            });
            exchange.getResponse().getCookies().add("color", ResponseCookie.from("color", "rojo").build());
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        }));
    }

    /**
     * Le da un orden a la ejecucion de los filtros, primero en entrar ultimo en salir de la cadena de filtros.
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}
