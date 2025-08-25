package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.mappers.UserEntityMapper;
import co.com.crediya.r2dbc.repositories.UserReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {

    @Mock
    private UserReactiveRepository reactiveRepository;

    @Mock
    private UserEntityMapper entityMapper;

    @InjectMocks
    private UserRepositoryAdapter userRepositoryAdapter;

    @Test
    void shouldSaveUserSuccessfully() {
        Mockito.when(this.entityMapper.toUserEntity(Mockito.any(User.class)))
                .thenReturn(UserEntity.builder().build());

        Mockito.when(this.reactiveRepository.save(Mockito.any(UserEntity.class)))
                .thenReturn(Mono.just(UserEntity.builder().build()));

        Mockito.when(this.entityMapper.toUserModel(Mockito.any(UserEntity.class)))
                .thenReturn(User.builder().build());

        StepVerifier.create(this.userRepositoryAdapter.saveUser(User.builder().build()))
                .expectNextCount(1L)
                .verifyComplete();

        Mockito.verify(this.entityMapper).toUserEntity(User.builder().build());
        Mockito.verify(this.reactiveRepository).save(UserEntity.builder().build());
        Mockito.verify(this.entityMapper).toUserModel(UserEntity.builder().build());

        Mockito.verifyNoMoreInteractions(this.entityMapper, this.reactiveRepository);
    }

    @Test
    void shouldLogErrorWhenSaveUserFails() {
        Mockito.when(this.entityMapper.toUserEntity(Mockito.any(User.class)))
                .thenThrow(new RuntimeException("To user entity mapper error."));

        StepVerifier.create(this.userRepositoryAdapter.saveUser(User.builder().build()))
                .verifyError(RuntimeException.class);

        Mockito.verify(this.entityMapper).toUserEntity(User.builder().build());

        Mockito.verifyNoMoreInteractions(this.entityMapper);

        Mockito.verifyNoInteractions(this.reactiveRepository);
    }

    @Test
    void shouldValidateUniqueEmailSuccessfully() {
        Mockito.when(this.reactiveRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(this.userRepositoryAdapter.validateUniqueEmail("carlosharo1986@gmail.com"))
                .expectNextCount(1L)
                .verifyComplete();

        Mockito.verify(this.reactiveRepository).existsByEmail("carlosharo1986@gmail.com");

        Mockito.verifyNoMoreInteractions(this.reactiveRepository);
        Mockito.verifyNoInteractions(this.entityMapper);
    }

    @Test
    void shouldLogErrorWhenValidateUniqueEmailFails() {
        Mockito.when(this.reactiveRepository.existsByEmail(Mockito.anyString()))
                .thenReturn(Mono.error(new RuntimeException("Exists by email reactive repository error.")));

        StepVerifier.create(this.userRepositoryAdapter.validateUniqueEmail("carlosharo1986@gmail.com"))
                .verifyError(RuntimeException.class);

        Mockito.verify(this.reactiveRepository).existsByEmail("carlosharo1986@gmail.com");

        Mockito.verifyNoMoreInteractions(this.reactiveRepository);
        Mockito.verifyNoInteractions(this.entityMapper);
    }
}
