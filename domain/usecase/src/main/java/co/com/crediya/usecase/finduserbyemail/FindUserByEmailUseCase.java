package co.com.crediya.usecase.finduserbyemail;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.exceptions.UserNotFoundException;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindUserByEmailUseCase {

    private final UserRepository userRepository;

    public Mono<User> findUserByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserNotFoundException(
                        "User not found with email %s".formatted(email))));
    }
}
