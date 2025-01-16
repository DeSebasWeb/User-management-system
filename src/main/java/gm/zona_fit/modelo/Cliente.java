package gm.zona_fit.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data//Se utiliza la libreria de lombok
//En este caso el data es para los metodos getters y setters
@NoArgsConstructor
@AllArgsConstructor
//Para poner un constructor vacio y otro con todos los atributos de la clase
@ToString
//El metodo toString
@EqualsAndHashCode

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    private String apellido;
    private Integer membresia;
}
