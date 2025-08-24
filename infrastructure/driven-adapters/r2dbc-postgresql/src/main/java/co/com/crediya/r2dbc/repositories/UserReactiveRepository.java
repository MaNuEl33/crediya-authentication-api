package co.com.crediya.r2dbc.repositories;

import co.com.crediya.r2dbc.entities.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long> {
    Mono<Boolean> existsByEmail(String email);
}
