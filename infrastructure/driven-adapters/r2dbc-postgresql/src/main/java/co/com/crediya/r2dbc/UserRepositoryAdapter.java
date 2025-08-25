package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.mappers.UserEntityMapper;
import co.com.crediya.r2dbc.repositories.UserReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Log4j2
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserReactiveRepository reactiveRepository;
    private final UserEntityMapper entityMapper;

    @Override
    public Mono<User> saveUser(User user) {
        return Mono.fromSupplier(() -> this.entityMapper.toUserEntity(user))
                .flatMap(this.reactiveRepository::save)
                .map(this.entityMapper::toUserModel)
                .doFirst(() -> log.info("Saving user in the database."))
                .doOnSuccess(u -> log.info("User saved successfully in the database."))
                .doOnError(err -> log.error("Error saving the user in the database: {}", err.getMessage()));
    }

    @Override
    public Mono<Boolean> validateUniqueEmail(String email) {
        return this.reactiveRepository.existsByEmail(email)
                .doFirst(() -> log.info("Validating whether the email is being used by another user."))
                .doOnSuccess(u -> log.info("The validation was successful."))
                .doOnError(err -> log.error("Error applying the validation: {}", err.getMessage()));
    }
}
