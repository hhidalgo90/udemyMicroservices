package cl.proyecto.gatewayserver.scapigateway.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * Configuracion de seguridad.
 */
@EnableWebFluxSecurity
public class SpringSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter authenticationFilter;
    /**
     * Protege todas las rutas configuradas en application.yaml
     * @param httpSecurity
     * @return
     */
    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .authorizeExchange()
                .pathMatchers("/servicioOauth/oauth/token").permitAll()//todos se pueden autenticar
                .pathMatchers(HttpMethod.GET, "/apiProducto/api/producto", "/apiItem/api/item", "/apiUsuario/usuarios").permitAll()
                .pathMatchers(HttpMethod.GET, "/apiProducto/api/producto/{id}", "/apiItem/api/item/{id}/**", "/apiUsuario/usuarios/{id}").hasAnyRole("ADMIN", "USER")
                .pathMatchers("/apiProducto/api/producto/**", "/apiItem/api/item/**", "/apiUsuario/usuarios/**").hasRole("ADMIN")
                .anyExchange()
                .authenticated()
                .and()
                .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf()
                .disable()
                .build();

    }
}
