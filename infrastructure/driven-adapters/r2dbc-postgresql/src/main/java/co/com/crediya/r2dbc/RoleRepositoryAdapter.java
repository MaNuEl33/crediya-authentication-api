package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.r2dbc.mappers.RoleEntityMapper;
import co.com.crediya.r2dbc.repositories.RoleReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log4j2
@RequiredArgsConstructor
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleReactiveRepository reactiveRepository;
    private final RoleEntityMapper entityMapper;

    @Override
    public Mono<Role> findById(Long id) {
        return this.reactiveRepository.findById(id)
                .map(this.entityMapper::toRoleModel)
                .doFirst(() -> log.info("Finding role by id in the database."))
                .doOnSuccess(role -> log.info("Role found in the database successfully."))
                .doOnError(err -> log.error("Error finding role by id in the database: {}", err.getMessage()));
    }
}
