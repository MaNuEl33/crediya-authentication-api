package co.com.crediya.usecase.searchusers;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.model.user.valueobjects.UserSearchCriteria;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class SearchUsersUseCase {

    private final UserRepository userRepository;

    public Flux<User> execute(UserSearchCriteria criteria) {
        return this.userRepository.searchUsers(criteria);
    }
}
