package cl.proyecto.oauth.client.services;

import brave.Tracer;
import cl.proyecto.oauth.client.UsuarioFeignClient;
import cl.proyecto.usuarios.commons.models.entity.Usuario;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {//Clase propia de spring security encargada de loguear a un usuario

    private Logger log = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioFeignClient cliente;

    @Autowired
    private Tracer tracer;

    /**
     * Loguea a un usuario mediante el ms usuarios y obtiene sus roles.
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario usuarioAutenticado = cliente.findByUsername(username);
            List<GrantedAuthority> authorities = usuarioAutenticado.getRoles()
                    .stream()
                    .map(role -> new SimpleGrantedAuthority(role.getNombre()))
                    .peek(simpleGrantedAuthority -> log.info("Role: " + simpleGrantedAuthority.getAuthority()))//se muestra en la consola cada role que tenga el usuario
                    .collect(Collectors.toList());

            log.info("Usuario autenticado: " + username);
            return new User(usuarioAutenticado.getUsername(), usuarioAutenticado.getPassword(), usuarioAutenticado.isEnabled(), true, true, true, authorities);
        } catch (FeignException e) {
            String error = "Error en el login, no existe usuario: " + username + " en el sistema.";
            log.error(error);

            tracer.currentSpan().tag("error.mensaje", error + " " + e.getMessage());
            throw new UsernameNotFoundException(error);
        }
    }

    @Override
    public Usuario findByUsername(String username) {
        return cliente.findByUsername(username);
    }

    @Override
    public Usuario update(Usuario usuario, Long id) {
        return cliente.update(usuario, id);
    }
}
