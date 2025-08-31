package co.com.crediya.usecase.loginuser;

import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.exceptions.UserBadCredentialsException;
import co.com.crediya.model.user.exceptions.UserLoginInvalidDataException;
import co.com.crediya.model.user.gateways.PasswordEncrypter;
import co.com.crediya.model.user.gateways.TokenProvider;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.helpers.FieldValidatorHelper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LogInUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncrypter passwordEncrypter;
    private final TokenProvider tokenProvider;

    public Mono<String> loginUser(String email, String rawPassword) {
        return this.validateInputData(email, rawPassword)
                .then(Mono.defer(() -> this.userRepository.findByEmail(email)))
                .filter(user -> this.passwordEncrypter.matches(rawPassword, user.getPassword()))
                .flatMap(this::addRole)
                .map(this.tokenProvider::generateToken)
                .switchIfEmpty(Mono.error(new UserBadCredentialsException("Invalid email or password.")));
    }

    private Mono<User> addRole(User user) {
        return this.roleRepository.findById(user.getRole().getId())
                .map(role -> user.toBuilder().role(role).build());
    }

    private Mono<Void> validateInputData(String email, String rawPassword) {
        return Mono.defer(() ->
                this.validateEmail(email).then(this.validateRawPassword(rawPassword)));
    }

    private Mono<Void> validateEmail(String email) {
        if (FieldValidatorHelper.isBlank(email)) {
            return Mono.error(new UserLoginInvalidDataException("The email is required or must not be empty."));
        }

        return Mono.empty();
    }

    private Mono<Void> validateRawPassword(String rawPassword) {
        if (FieldValidatorHelper.isBlank(rawPassword)) {
            return Mono.error(new UserLoginInvalidDataException("The password is required or must not be empty."));
        }

        return Mono.empty();
    }
}
