package co.com.crediya.api.handlers;

import co.com.crediya.api.dtos.ErrorResponseDto;
import co.com.crediya.api.exceptions.ValidationException;
import co.com.crediya.usecase.user.exceptions.RoleNotFoundException;
import co.com.crediya.usecase.user.exceptions.UserDuplicateEmailException;
import co.com.crediya.usecase.user.exceptions.UserInvalidDataException;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@UtilityClass
public class GlobalErrorHandler {

    public static HandlerFilterFunction<ServerResponse, ServerResponse> errorHandler() {
        return (request, next) -> next.handle(request)
                .onErrorResume(ValidationException.class, GlobalErrorHandler::handleValidationException)
                .onErrorResume(UserInvalidDataException.class, GlobalErrorHandler::handleUserInvalidDataException)
                .onErrorResume(RoleNotFoundException.class, GlobalErrorHandler::handleRoleNotFoundException)
                .onErrorResume(UserDuplicateEmailException.class, GlobalErrorHandler::handleUserDuplicateEmailException)
                .onErrorResume(Exception.class, GlobalErrorHandler::handleUnexpectedException);
    }

    private static Mono<ServerResponse> handleValidationException(ValidationException e) {
        final var errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getViolations().stream()
                        .map(v -> v.getPropertyPath() + " " + v.getMessage())
                        .collect(Collectors.joining(", "))
                ).build();

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    private static Mono<ServerResponse> handleUserInvalidDataException(UserInvalidDataException e) {
        final var errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(e.getMessage())
                .build();

        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    private static Mono<ServerResponse> handleRoleNotFoundException(RoleNotFoundException e) {
        final var errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();

        return ServerResponse.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    private static Mono<ServerResponse> handleUserDuplicateEmailException(UserDuplicateEmailException e) {
        final var errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(e.getMessage())
                .build();

        return ServerResponse.status(HttpStatus.CONFLICT)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }

    private static Mono<ServerResponse> handleUnexpectedException(Exception e) {
        final var errorResponse = ErrorResponseDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(e.getMessage())
                .build();

        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorResponse);
    }
}
