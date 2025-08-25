package co.com.crediya.usecase.user;

import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.helpers.FieldValidatorHelper;
import co.com.crediya.usecase.user.exceptions.RoleNotFoundException;
import co.com.crediya.usecase.user.exceptions.UserDuplicateEmailException;
import co.com.crediya.usecase.user.exceptions.UserInvalidDataException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Objects;

@RequiredArgsConstructor
public class UserUseCase {

    private static final BigDecimal MIN_BASE_SALARY = BigDecimal.ZERO;
    private static final BigDecimal MAX_BASE_SALARY = BigDecimal.valueOf(15000000);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public Mono<User> registerUser(User user) {
        return this.validateUser(user)
                .flatMap(this::validateExistingRole)
                .flatMap(this::validateUniqueEmail)
                .flatMap(this.userRepository::saveUser);
    }

    private Mono<User> validateUser(User user) {
        if (Objects.isNull(user)) {
            return Mono.error(new UserInvalidDataException("The user is required."));
        }

        return Mono.just(user)
                .filter(u -> FieldValidatorHelper.isNotBlank(u.getFirstName()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The first name of the user is required.")))
                .filter(u -> FieldValidatorHelper.isNotBlank(u.getLastName()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The last name of the user is required.")))
                .filter(u -> FieldValidatorHelper.isNotBlank(u.getEmail()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The email of the user is required.")))
                .filter(u -> FieldValidatorHelper.isValidEmail(u.getEmail()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The email of the user is not valid.")))
                .filter(u -> Objects.nonNull(u.getBaseSalary()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The base salary of the user is required.")))
                .filter(u -> FieldValidatorHelper.isInRange(u.getBaseSalary(), MIN_BASE_SALARY, MAX_BASE_SALARY))
                .switchIfEmpty(Mono.error(new UserInvalidDataException(
                        "The base salary range of the user must be between %s and %s."
                                .formatted(MIN_BASE_SALARY, MAX_BASE_SALARY))))
                .filter(u -> Objects.nonNull(u.getRole()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The role of the user is required.")))
                .filter(u -> Objects.nonNull(u.getRole().getId()))
                .switchIfEmpty(Mono.error(new UserInvalidDataException("The id role of the user is required.")));
    }

    private Mono<User> validateExistingRole(User user) {
        return this.roleRepository.findById(user.getRole().getId())
                .switchIfEmpty(Mono.error(new RoleNotFoundException("The role to be assigned to the user must exist.")))
                .thenReturn(user);
    }

    private Mono<User> validateUniqueEmail(User user) {
        return this.userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> Boolean.TRUE.equals(exists)
                        ? Mono.error(new UserDuplicateEmailException("The email of the user is already exists."))
                        : Mono.just(user)
                );
    }
}
