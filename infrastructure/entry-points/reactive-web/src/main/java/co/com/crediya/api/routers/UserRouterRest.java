package co.com.crediya.api.routers;

import co.com.crediya.api.config.UserPath;
import co.com.crediya.api.handlers.GlobalErrorHandler;
import co.com.crediya.api.handlers.UserHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UserRouterRest {

    private final UserHandler userHandler;
    private final UserPath userPath;

    @Bean
    public RouterFunction<ServerResponse> userRoutes() {
        return route(POST(this.userPath.getRegister()), this.userHandler::listenRegisterUser)
                .filter(GlobalErrorHandler.errorHandler());
    }
}
