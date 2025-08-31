package co.com.crediya.api.handlers;

import co.com.crediya.api.dtos.LoginUserRequestDto;
import co.com.crediya.api.helpers.ValidatorHelper;
import co.com.crediya.api.mappers.LoginUserDtoMapper;
import co.com.crediya.usecase.loginuser.LogInUserUseCase;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class LoginUserHandler {

    private final LogInUserUseCase useCase;
    private final LoginUserDtoMapper dtoMapper;
    private final Validator validator;

    public Mono<ServerResponse> listenLoginUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LoginUserRequestDto.class)
                .transform(requestBody -> ValidatorHelper.validateObject(requestBody, validator))
                .flatMap(requestDto -> this.useCase.loginUser(requestDto.email(), requestDto.password()))
                .map(this.dtoMapper::toLoginUserResponseDto)
                .flatMap(r -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(r))
                .doFirst(() -> log.info("New request to login a user."))
                .doOnSuccess(r -> log.info("The user has successfully logged in."))
                .doOnError(err -> log.error("The user has not successfully logged in.", err));
    }
}
