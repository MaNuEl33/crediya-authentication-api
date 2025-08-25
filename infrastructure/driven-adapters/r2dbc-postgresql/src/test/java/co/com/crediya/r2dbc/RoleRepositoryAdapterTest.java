package co.com.crediya.r2dbc;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entities.RoleEntity;
import co.com.crediya.r2dbc.mappers.RoleEntityMapper;
import co.com.crediya.r2dbc.repositories.RoleReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryAdapterTest {

    @Mock
    private RoleReactiveRepository reactiveRepository;

    @Mock
    private RoleEntityMapper entityMapper;

    @InjectMocks
    private RoleRepositoryAdapter roleRepositoryAdapter;

    @Test
    void shouldFindByIdSuccessfully() {
        Mockito.when(this.reactiveRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.just(RoleEntity.builder().build()));

        Mockito.when(this.entityMapper.toRoleModel(Mockito.any(RoleEntity.class)))
                .thenReturn(Role.builder().build());

        StepVerifier.create(this.roleRepositoryAdapter.findById(1L))
                .expectNextCount(1L)
                .verifyComplete();

        Mockito.verify(this.reactiveRepository).findById(1L);
        Mockito.verify(this.entityMapper).toRoleModel(RoleEntity.builder().build());

        Mockito.verifyNoMoreInteractions(this.reactiveRepository, this.entityMapper);
    }

    @Test
    void shouldLogErrorWhenFindByIdFails() {
        Mockito.when(this.reactiveRepository.findById(Mockito.anyLong()))
                .thenReturn(Mono.error(new RuntimeException("Find by id reactive repository error.")));

        StepVerifier.create(this.roleRepositoryAdapter.findById(1L))
                .verifyError(RuntimeException.class);

        Mockito.verify(this.reactiveRepository).findById(1L);

        Mockito.verifyNoMoreInteractions(this.reactiveRepository);
        Mockito.verifyNoInteractions(this.entityMapper);
    }
}
