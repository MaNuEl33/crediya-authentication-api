package co.com.crediya.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String address;
    private String email;
    private String phoneNumber;
    private String documentNumber;
    private BigDecimal baseSalary;
}
