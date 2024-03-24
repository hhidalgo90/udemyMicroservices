package cl.proyecto.oauth.client.services;

import cl.proyecto.usuarios.commons.models.entity.Usuario;

public interface IUsuarioService {

    public Usuario findByUsername(String username);

    public Usuario update(Usuario usuario, Long id);
}
