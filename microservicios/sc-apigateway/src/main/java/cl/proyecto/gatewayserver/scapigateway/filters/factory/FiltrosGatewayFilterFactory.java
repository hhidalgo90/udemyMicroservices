package cl.proyecto.gatewayserver.scapigateway.filters.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Clase que crea filtro personalizado, al usar el prefijo GatewayFilterFactory springboot la detecta automaticamente como filtro, se configura en application.yaml
 */
@Component
public class FiltrosGatewayFilterFactory extends AbstractGatewayFilterFactory<FiltrosGatewayFilterFactory.Configuracion> {//creamos subclase
    private final Logger logger = LoggerFactory.getLogger(FiltrosGatewayFilterFactory.class);

    public FiltrosGatewayFilterFactory() {
        super(Configuracion.class);
    }

    @Override
    public GatewayFilter apply(Configuracion config) {
        //OrderedGatewayFilter le da un orden de ejecucion a los filtros. En caso de que se quiera dar un orden.
        return new OrderedGatewayFilter(((exchange, chain) -> {
            logger.info("Ejecutando pre gateway filter factory: " + config.mensaje);
            return chain.filter(exchange).then(Mono.fromRunnable(()-> {

                Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
                    exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
                });

                logger.info("Ejecutando post gateway filter factory: " + config.mensaje);
            }));
        }),2);
    }

    /**
     * Personalizar nombre de la clase filtro
     * @return
     */
    @Override
    public String name() {
        return "FiltroPersonalizado";
    }

    /**
     * Subclase de configuracion
     */
    static class Configuracion {
        private String mensaje;
        private String cookieValor;
        private String cookieNombre;

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getCookieValor() {
            return cookieValor;
        }

        public void setCookieValor(String cookieValor) {
            this.cookieValor = cookieValor;
        }

        public String getCookieNombre() {
            return cookieNombre;
        }

        public void setCookieNombre(String cookieNombre) {
            this.cookieNombre = cookieNombre;
        }
    }
}
