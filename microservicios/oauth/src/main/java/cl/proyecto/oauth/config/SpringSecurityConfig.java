package cl.proyecto.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Spring automaticamente va a buscar una implementacion de esta clase y encontrara la que esta en UsuarioService
     * ya que esta es anotada con @Service
     */
    @Autowired
    UserDetailsService userDetailsService;

    /**
     * Se obtiene interfaz de la clase LoginHandler para registrar los eventos al loguear.
     */
    @Autowired
    AuthenticationEventPublisher eventPublisher;

    /**
     * Authentication manager, registra el userDetailsService y encripta la pass del usuario, cuando usuario se loguea automaticamente la pass queda encriptada.
     * @param auth
     * @throws Exception
     */
    @Override
    @Autowired //Para poder inyectar el parametro AuthenticationManagerBuilder en el metodo
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
                .and().authenticationEventPublisher(eventPublisher);//Se registra manejador de eventos del login.
        super.configure(auth);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean //se registra via metodo en el contenedor de spring.
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
