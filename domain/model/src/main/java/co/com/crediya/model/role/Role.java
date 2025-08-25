package co.com.crediya.model.role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class Role {
    private Long id;
    private String name;
    private String description;
}
