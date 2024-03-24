package cl.proyecto.zuul.zuulapigateway.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Base64;

/**
 * Servidor de recursos
 */
@RefreshScope //Para quee funcione con actuator y refrar en tiempo real las configuraciones
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Value("${config.security.oauth.jwt.key}")
    private String jwt_key;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }

    /**
     * Configuracion de acceso a rutas mediante roles de usuario, ademas permite que cualquier usuario se pueda autenticar.
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/servicioOauth/oauth/token").permitAll()//todos se pueden autenticar
                .antMatchers(HttpMethod.GET, "/apiProducto/api/producto", "/apiItem/api/item", "/apiUsuario/usuarios").permitAll()
                .antMatchers(HttpMethod.GET, "/apiProducto/api/producto/{id}"
                        , "/apiItem/api/item/{id}/**", "/apiUsuario/usuarios/{id}").hasAnyRole("ADMIN", "USER")//va sin el prefijo ROLE_
                .antMatchers("/apiProducto/api/producto/**", "/apiItem/api/item/**", "/apiUsuario/usuarios/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .cors().configurationSource(configuracionCors());

    }

    /**
     * Configurcion de cors para clientes front como angular, react, js , etc.
     * @return
     */
    @Bean
    public CorsConfigurationSource configuracionCors(){
        CorsConfiguration cors = new CorsConfiguration();
        cors.addAllowedOrigin("*");
        cors.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE","OPTIONS"));
        cors.setAllowCredentials(true);
        cors.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors); //registramos la configuracion del cors para todos nuestros endpoints
        return source;
    }

    /**
     * Filtro de cors para toda la aplicacion en general (no solo para el contexto de spring security, queda de forma global),
     * para cada endpoint pasara por este filtro de cors.
     * @return
     */
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(configuracionCors()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(Base64.getEncoder().encodeToString(jwt_key.getBytes()));  //clave secreta del servidor
        return jwtAccessTokenConverter;
    }
}
