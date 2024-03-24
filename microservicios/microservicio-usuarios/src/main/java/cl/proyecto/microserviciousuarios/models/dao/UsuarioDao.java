package cl.proyecto.microserviciousuarios.models.dao;

import cl.proyecto.usuarios.commons.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

//PagingAndSortingRepository hereda de crudrepository pero trae funcionalidades de sorting y paginacion

@RepositoryRestResource(path = "usuarios") //api rest automatica
public interface UsuarioDao extends PagingAndSortingRepository<Usuario,Long> {

   // @RestResource(path = "buscar-username")//personalizamos nombre del recurso y nombre de parametro para buscarlo por la ruta que queramos
    //public Usuario findByUsername(@Param("nombreUsuario") String username);

    @RestResource(path = "buscar-username")
    public Usuario findByUsername(String username);

    @Query("select u from Usuario u where u.username=?1")
    public Usuario obtenerPorUsername(String username);
}
