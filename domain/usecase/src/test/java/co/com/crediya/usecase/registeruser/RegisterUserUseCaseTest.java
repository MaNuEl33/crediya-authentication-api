package co.com.crediya.usecase.registeruser;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.PasswordEncrypter;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.model.role.exceptions.RoleNotFoundException;
import co.com.crediya.model.user.exceptions.UserDuplicateEmailException;
import co.com.crediya.model.user.exceptions.UserRegistrationInvalidDataException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    private static final String FIRST_NAME = "Manuel";
    private static final String LAST_NAME = "Haro";
    private static final String EMAIL = "manuelharo1994@gmail.com";
    private static final String PWD = "mi_poderoso_password";
    private static final String PWD_ENCRYPTED = "mi_poderoso_password_encrypted";

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncrypter passwordEncrypter;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void shouldRegisterUserSuccessfully() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        final var toSaveUser = user.toBuilder()
                .password(PWD_ENCRYPTED)
                .build();

        final var savedUser = toSaveUser.toBuilder()
                .id(1L)
                .build();

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(Role.builder().id(1L).build()));

        Mockito.when(this.userRepository.validateUniqueEmail(Mockito.anyString()))
                .thenReturn(Mono.just(false));

        Mockito.when(this.passwordEncrypter.encrypt(Mockito.anyString()))
                .thenReturn(PWD_ENCRYPTED);

        Mockito.when(this.userRepository.saveUser(Mockito.any(User.class)))
                .thenReturn(Mono.just(savedUser));

        StepVerifier.create(this.useCase.registerUser(user))
                .expectNext(savedUser)
                .verifyComplete();

        Mockito.verify(this.roleRepository).findById(1L);
        Mockito.verify(this.userRepository).validateUniqueEmail("manuelharo1994@gmail.com");
        Mockito.verify(this.passwordEncrypter).encrypt(PWD);
        Mockito.verify(this.userRepository).saveUser(toSaveUser);

        Mockito.verifyNoMoreInteractions(this.roleRepository, this.userRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenIsNull() {
        StepVerifier.create(this.useCase.registerUser(null))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenFirstNameIsBlank() {
        final var user = User.builder()
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenLastNameIsBlank() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenEmailIsBlank() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenEmailIsInvalid() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email("manuelharo")
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenBaseSalaryIsNull() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenBaseSalaryExceedsTheMaximum() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(15000010))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenBaseSalaryIsLessThanTheMinimum() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(-1))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenRoleIsNull() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenRoleIdIsNull() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder().build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenRoleDoesNotExist() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(RoleNotFoundException.class);

        Mockito.verify(this.roleRepository).findById(1L);

        Mockito.verifyNoMoreInteractions(this.roleRepository);
        Mockito.verifyNoInteractions(this.userRepository, this.passwordEncrypter);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenEmailIsAlreadyInUse() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PWD)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(Role.builder().id(1L).build()));

        Mockito.when(this.userRepository.validateUniqueEmail(Mockito.anyString()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserDuplicateEmailException.class);

        Mockito.verify(this.roleRepository).findById(1L);
        Mockito.verify(this.userRepository).validateUniqueEmail("manuelharo1994@gmail.com");

        Mockito.verifyNoMoreInteractions(this.roleRepository, this.userRepository, this.passwordEncrypter);
    }

    @Test
    void shouldNotRegisterUserWhenPasswordIsBlank() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }

    @Test
    void shouldNotRegisterUserWhenSizeEmailIsLowerThanSix() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password("12345")
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.useCase.registerUser(user))
                .verifyError(UserRegistrationInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository, this.passwordEncrypter);
    }
}
