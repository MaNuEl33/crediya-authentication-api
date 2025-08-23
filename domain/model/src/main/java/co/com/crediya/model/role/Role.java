package co.com.crediya.model.role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Role {
    private Long id;
    private String name;
    private String description;
}
