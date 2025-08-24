package co.com.crediya.r2dbc.repositories;

import co.com.crediya.r2dbc.entities.RoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long> {
}
