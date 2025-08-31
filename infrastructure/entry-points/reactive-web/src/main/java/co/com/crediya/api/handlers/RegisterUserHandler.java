package co.com.crediya.api.handlers;

import co.com.crediya.api.dtos.RegisterUserRequestDto;
import co.com.crediya.api.helpers.ValidatorHelper;
import co.com.crediya.api.mappers.RegisterUserDtoMapper;
import co.com.crediya.usecase.registeruser.RegisterUserUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class RegisterUserHandler {

    private final RegisterUserUseCase useCase;
    private final RegisterUserDtoMapper dtoMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenRegisterUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterUserRequestDto.class)
                .transform(requestBody -> ValidatorHelper.validateObject(requestBody, validator))
                .map(this.dtoMapper::toUserModel)
                .flatMap(this.useCase::registerUser)
                .map(this.dtoMapper::toRegisterUserResponseDto)
                .flatMap(u -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(u))
                .doFirst(() -> log.info("New request to register a user."))
                .doOnSuccess(r -> log.info("The user registration request has been successfully."))
                .doOnError(err -> log.error("The user registration request has been failed.", err));
    }
}
