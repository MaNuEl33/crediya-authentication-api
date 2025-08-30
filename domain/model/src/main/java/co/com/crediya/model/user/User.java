package co.com.crediya.model.user;
import co.com.crediya.model.role.Role;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String email;
    private String password;
    private String phoneNumber;
    private String documentNumber;
    private BigDecimal baseSalary;
    private Role role;
}
