package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.valueobjects.UserSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> saveUser(User user);
    Mono<Boolean> validateUniqueEmail(String email);
    Mono<User> findByEmail(String email);
    Flux<User> searchUsers(UserSearchCriteria criteria);
}
