package co.com.crediya.api.routers;

import co.com.crediya.api.config.UserPath;
import co.com.crediya.api.dtos.RegisterUserRequestDto;
import co.com.crediya.api.dtos.RegisterUserResponseDto;
import co.com.crediya.api.handlers.UserHandler;
import co.com.crediya.api.mappers.UserDtoMapper;
import co.com.crediya.model.user.User;
import co.com.crediya.usecase.registeruser.RegisterUserUseCase;
import co.com.crediya.usecase.registeruser.exceptions.RoleNotFoundException;
import co.com.crediya.usecase.registeruser.exceptions.UserDuplicateEmailException;
import co.com.crediya.usecase.registeruser.exceptions.UserInvalidDataException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@ContextConfiguration(classes = {UserRouterRest.class, UserHandler.class})
@EnableConfigurationProperties(UserPath.class)
@WebFluxTest
@ActiveProfiles("test")
class UserRouterRestTest {

    private static final String PATH = "/api/v1/usuarios";

    private static final String FIRST_NAME = "Anggie";
    private static final String LAST_NAME = "Yengle";
    private static final String EMAIL = "anggieyengle2000@mail.com";
    private static final String PWD = "mi_poderoso_password_2";

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private RegisterUserUseCase useCase;

    @MockitoBean
    private UserDtoMapper dtoMapper;

    @Test
    void shouldRegisterUserSuccessfully() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        final var responseBody = new RegisterUserResponseDto(
                1L, FIRST_NAME, LAST_NAME, null, null, EMAIL, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        Mockito.when(this.dtoMapper.toUserModel(Mockito.any(RegisterUserRequestDto.class)))
                .thenReturn(User.builder().build());

        Mockito.when(this.useCase.registerUser(Mockito.any(User.class)))
                .thenReturn(Mono.just(User.builder().build()));

        Mockito.when(this.dtoMapper.toRegisterUserResponseDto(Mockito.any(User.class)))
                .thenReturn(responseBody);

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(this.dtoMapper).toUserModel(requestBody);
        Mockito.verify(this.useCase).registerUser(User.builder().build());
        Mockito.verify(this.dtoMapper).toRegisterUserResponseDto(User.builder().build());

        Mockito.verifyNoMoreInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfFirstNameIsBlank() {
        final var requestBody = new RegisterUserRequestDto(
                null, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfLastNameIsBlank() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, null, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfEmailIsBlank() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, null, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfEmailIsInvalid() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, "ayengle", PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfBaseSalaryIsNull() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, null, 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfBaseSalaryExceedsTheLimit() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(100000000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfBaseSalaryIsLessThanTheLimit() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(-10), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfRoleIdIsNull() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), null
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfUserInvalidDataExceptionIsThrown() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        Mockito.when(this.dtoMapper.toUserModel(Mockito.any(RegisterUserRequestDto.class)))
                .thenReturn(User.builder().build());

        Mockito.when(this.useCase.registerUser(Mockito.any(User.class)))
                .thenReturn(Mono.error(new UserInvalidDataException("User invalid data exception.")));

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verify(this.dtoMapper).toUserModel(requestBody);
        Mockito.verify(this.useCase).registerUser(User.builder().build());

        Mockito.verifyNoMoreInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfRoleNotFoundExceptionIsThrown() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        Mockito.when(this.dtoMapper.toUserModel(Mockito.any(RegisterUserRequestDto.class)))
                .thenReturn(User.builder().build());

        Mockito.when(this.useCase.registerUser(Mockito.any(User.class)))
                .thenReturn(Mono.error(new RoleNotFoundException("Role not found exception.")));

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        Mockito.verify(this.dtoMapper).toUserModel(requestBody);
        Mockito.verify(this.useCase).registerUser(User.builder().build());

        Mockito.verifyNoMoreInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfUserDuplicateEmailExceptionIsThrown() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        Mockito.when(this.dtoMapper.toUserModel(Mockito.any(RegisterUserRequestDto.class)))
                .thenReturn(User.builder().build());

        Mockito.when(this.useCase.registerUser(Mockito.any(User.class)))
                .thenReturn(Mono.error(new UserDuplicateEmailException("User duplicate email exception.")));

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        Mockito.verify(this.dtoMapper).toUserModel(requestBody);
        Mockito.verify(this.useCase).registerUser(User.builder().build());

        Mockito.verifyNoMoreInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfUnexpectedExceptionIsThrown() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, PWD, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        Mockito.when(this.dtoMapper.toUserModel(Mockito.any(RegisterUserRequestDto.class)))
                .thenReturn(User.builder().build());

        Mockito.when(this.useCase.registerUser(Mockito.any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("Unexpected exception.")));

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.verify(this.dtoMapper).toUserModel(requestBody);
        Mockito.verify(this.useCase).registerUser(User.builder().build());

        Mockito.verifyNoMoreInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserIfPasswordIsBlank() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, null, null,
                null, BigDecimal.valueOf(4000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }

    @Test
    void shouldNotRegisterUserWhenSizeEmailIsLowerThanSix() {
        final var requestBody = new RegisterUserRequestDto(
                FIRST_NAME, LAST_NAME, null, null, EMAIL, "12345", null,
                null, BigDecimal.valueOf(4000), 1L
        );

        this.webTestClient.post()
                .uri(PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange()
                .expectStatus().isBadRequest();

        Mockito.verifyNoInteractions(this.dtoMapper, this.useCase);
    }
}
