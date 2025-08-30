package co.com.crediya.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
public class UserEntity {

    @Id
    @Column("id_usuario")
    private Long id;

    @Column("nombre")
    private String firstName;

    @Column("apellido")
    private String lastName;

    @Column("fecha_nacimiento")
    private LocalDate birthDate;

    @Column("direccion")
    private String address;

    private String email;

    private String password;

    @Column("telefono")
    private String phoneNumber;

    @Column("documento_identidad")
    private String documentNumber;

    @Column("salario_base")
    private BigDecimal baseSalary;

    @Column("id_rol")
    private Long roleId;
}
