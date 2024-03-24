package cl.proyecto.usuarios.commons.models.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 20)
    private String username;

    @Column(unique = true, length = 60)
    private String password;

    private String nombre;
    private String apellido;
    @Column(unique = true)
    private String email;

    private boolean enabled;

    @Column(name = "nro_intentos")
    private Integer nrointentos;

    @ManyToMany(fetch = FetchType.LAZY) //estrategia para obtener datos, carga perezosa
    @JoinTable(name = "usuarios_to_roles", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "role_id"),
    uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "role_id"})})//customizar el nombre de las columnas
    private List<Role> roles;
}
