package co.com.crediya.api.handlers;

import co.com.crediya.api.dtos.SearchUsersResponseDto;
import co.com.crediya.api.mappers.SearchUsersDtoMapper;
import co.com.crediya.model.user.valueobjects.UserSearchCriteria;
import co.com.crediya.usecase.searchusers.SearchUsersUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Log4j2
public class SearchUsersHandler {

    private final SearchUsersUseCase searchUsersUseCase;
    private final SearchUsersDtoMapper dtoMapper;

    public Mono<ServerResponse> listenSearchUsers(ServerRequest serverRequest) {
        final var email = serverRequest.queryParam("email").orElse(null);
        final var criteria = new UserSearchCriteria(email);

        final var usersFlux = this.searchUsersUseCase.execute(criteria)
                .map(this.dtoMapper::toSearchUsersResponseDto);

        return ServerResponse.ok()
                .body(usersFlux, SearchUsersResponseDto.class)
                .doFirst(() -> log.info("New request to search users."))
                .doOnSuccess(r -> log.info("The user search was successful."))
                .doOnError(err -> log.error("Error searching users.", err));
    }
}
