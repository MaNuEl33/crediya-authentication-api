package co.com.crediya.usecase.user;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.user.exceptions.RoleNotFoundException;
import co.com.crediya.usecase.user.exceptions.UserDuplicateEmailException;
import co.com.crediya.usecase.user.exceptions.UserInvalidDataException;
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
class UserUseCaseTest {

    private static final String FIRST_NAME = "Manuel";
    private static final String LAST_NAME = "Haro";
    private static final String EMAIL = "manuelharo1994@gmail.com";

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    @Test
    void shouldRegisterUserSuccessfully() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        final var savedUser = User.builder()
                .id(1L)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(Role.builder().id(1L).build()));

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(false));

        Mockito.when(this.userRepository.saveUser(Mockito.any(User.class)))
                .thenReturn(Mono.just(savedUser));

        StepVerifier.create(this.userUseCase.registerUser(user))
                .expectNext(savedUser)
                .verifyComplete();

        Mockito.verify(this.roleRepository).findById(1L);
        Mockito.verify(this.userRepository).existsByEmail("manuelharo1994@gmail.com");
        Mockito.verify(this.userRepository).saveUser(user);

        Mockito.verifyNoMoreInteractions(this.roleRepository, this.userRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenIsNull() {
        StepVerifier.create(this.userUseCase.registerUser(null))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenFirstNameIsBlank() {
        final var user = User.builder()
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenLastNameIsBlank() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenEmailIsBlank() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenEmailIsInvalid() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email("manuelharo")
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenBaseSalaryIsNull() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenBaseSalaryExceedsTheMaximum() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(15000010))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenBaseSalaryIsLessThanTheMinimum() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(-1))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenRoleIsNull() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenRoleIdIsNull() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder().build())
                .build();

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository,  this.roleRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenRoleDoesNotExist() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(RoleNotFoundException.class);

        Mockito.verify(this.roleRepository).findById(1L);

        Mockito.verifyNoMoreInteractions(this.roleRepository);
        Mockito.verifyNoInteractions(this.userRepository);
    }

    @Test
    void shouldRegisterUserUnsuccessfullyWhenEmailIsAlreadyInUse() {
        final var user = User.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(Role.builder().id(1L).build()));

        Mockito.when(this.userRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(this.userUseCase.registerUser(user))
                .verifyError(UserDuplicateEmailException.class);

        Mockito.verify(this.roleRepository).findById(1L);
        Mockito.verify(this.userRepository).existsByEmail("manuelharo1994@gmail.com");

        Mockito.verifyNoMoreInteractions(this.roleRepository, this.userRepository);
    }
}
