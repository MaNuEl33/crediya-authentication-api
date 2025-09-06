package co.com.crediya.usecase.loginuser;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.role.gateways.RoleRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.exceptions.UserBadCredentialsException;
import co.com.crediya.model.user.exceptions.UserLoginInvalidDataException;
import co.com.crediya.model.user.gateways.PasswordEncrypter;
import co.com.crediya.model.user.gateways.TokenProvider;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class LogInUserUseCaseTest {

    private static final String EMAIL = "mharo@gmail.com";
    private static final String PWD_DECRYPTED = "mi_poderoso_password";
    private static final String PWD_ENCRYPTED = "mi_poderoso_password_encrypted";
    private static final String TOKEN = "token";

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncrypter passwordEncrypter;

    @Mock
    private TokenProvider tokenProvider;

    @InjectMocks
    private LogInUserUseCase useCase;

    private User defaultUser;

    private Role defaultRole;

    @BeforeEach
    void setUp() {
        this.defaultUser = User.builder()
                .email(EMAIL)
                .password(PWD_ENCRYPTED)
                .role(Role.builder()
                        .id(1L)
                        .build())
                .build();

        this.defaultRole = Role.builder()
                .id(1L)
                .name("ADMIN")
                .build();
    }

    @Test
    void shouldLogInUserSuccessfully() {
        final var userWithRole = this.defaultUser.toBuilder()
                .role(this.defaultRole.toBuilder().build())
                .build();

        Mockito.when(this.userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(this.defaultUser));

        Mockito.when(this.passwordEncrypter.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        Mockito.when(this.roleRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(this.defaultRole));

        Mockito.when(this.tokenProvider.generateToken(Mockito.any(User.class)))
                .thenReturn(TOKEN);

        StepVerifier.create(this.useCase.loginUser(EMAIL, PWD_DECRYPTED))
                .expectNext(TOKEN)
                .verifyComplete();

        Mockito.verify(this.userRepository).findByEmail(EMAIL);
        Mockito.verify(this.passwordEncrypter).matches(PWD_DECRYPTED, PWD_ENCRYPTED);
        Mockito.verify(this.roleRepository).findById(1L);
        Mockito.verify(this.tokenProvider).generateToken(userWithRole);

        Mockito.verifyNoMoreInteractions(this.userRepository, this.roleRepository, this.tokenProvider);
    }

    @Test
    void shouldNotLogInUserWhenEmailIsBlank() {
        StepVerifier.create(this.useCase.loginUser(null, PWD_DECRYPTED))
                .verifyError(UserLoginInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository, this.roleRepository, this.tokenProvider);
    }

    @Test
    void shouldNotLogInUserWhenPasswordIsBlank() {
        StepVerifier.create(this.useCase.loginUser(EMAIL, null))
                .verifyError(UserLoginInvalidDataException.class);

        Mockito.verifyNoInteractions(this.userRepository, this.roleRepository, this.tokenProvider);
    }

    @Test
    void shouldNotLogInUserWhenPasswordIsIncorrect() {
        Mockito.when(this.userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(this.defaultUser));

        Mockito.when(this.passwordEncrypter.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);

        StepVerifier.create(this.useCase.loginUser(EMAIL, "wrong_password"))
                .verifyError(UserBadCredentialsException.class);

        Mockito.verify(this.userRepository).findByEmail(EMAIL);
        Mockito.verify(this.passwordEncrypter).matches("wrong_password", PWD_ENCRYPTED);

        Mockito.verifyNoMoreInteractions(this.userRepository, this.tokenProvider);
        Mockito.verifyNoInteractions(this.roleRepository);
    }

    @Test
    void shouldNotLogInUserWhenUserDoesNotExist() {
        Mockito.when(this.userRepository.findByEmail(Mockito.anyString()))
                .thenReturn(Mono.empty());

        StepVerifier.create(this.useCase.loginUser("unknown_email@mail.com", PWD_DECRYPTED))
                .verifyError(UserBadCredentialsException.class);

        Mockito.verify(this.userRepository).findByEmail("unknown_email@mail.com");

        Mockito.verifyNoMoreInteractions(this.userRepository);
        Mockito.verifyNoInteractions(this.tokenProvider, this.roleRepository);
    }
}
