package cl.proyecto.oauth.client.security.event;

import brave.Tracer;
import cl.proyecto.oauth.client.services.IUsuarioService;
import cl.proyecto.usuarios.commons.models.entity.Usuario;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;


/**
 * Clase que maneja los eventos cuando un usuario se loguea con exito o con error.
 * Realiza una accion en cada caso.
 */
@Component
public class LoginHandler implements AuthenticationEventPublisher {
    private Logger log = LoggerFactory.getLogger(LoginHandler.class);

    @Autowired
    private IUsuarioService usuarioService;

    private static final Integer LIMITE_INTENTOS = 3;

    @Autowired
    private Tracer tracer;
    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
        if (authentication.getDetails() instanceof WebAuthenticationDetails) { //este if es para que no retorne el client id, frontend_app y solo retorne el usuario logueado
            return;
        }
        UserDetails usuario = (UserDetails) authentication.getPrincipal();

        Usuario user = usuarioService.findByUsername((authentication.getName()));

        if(user.getNrointentos() != null && user.getNrointentos() > 0){
            user.setNrointentos(0);
            usuarioService.update(user , user.getId());

        }
        log.info("Usuario logueado con exito!: " + usuario.getUsername());

    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {

        try {
            StringBuilder builder = new StringBuilder();
            Usuario usuario = usuarioService.findByUsername((authentication.getName()));

            if (usuario.getNrointentos() >= LIMITE_INTENTOS) {
                usuario.setEnabled(false);
                log.error("Error en el login: Usuario bloqueado" + exception.getMessage());
                builder.append("Error en el login: Usuario bloqueado" + exception.getMessage());
            }
            usuario.setNrointentos(usuario.getNrointentos() + 1);

            usuarioService.update(usuario, usuario.getId());

            log.error("Error en el login: " + exception.getMessage());
            builder.append("Error en el login: " + exception.getMessage());
            tracer.currentSpan().tag("error.login", builder.toString());
        } catch (FeignException e) {
            log.error(String.format("Usuario %s no existe! " , authentication.getName()));
        }
    }
}
