package co.com.crediya.r2dbc.mappers;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entities.RoleEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class RoleEntityMapperTest {

    private final RoleEntityMapper mapper = Mappers.getMapper(RoleEntityMapper.class);

    @Test
    void shouldMapToRoleModel() {
        final var roleEntity = RoleEntity.builder()
                .id(1L)
                .name("Role")
                .description("Description")
                .build();

        final var expectedRoleModel = Role.builder()
                .id(1L)
                .name("Role")
                .description("Description")
                .build();

        final var actualRoleModel = this.mapper.toRoleModel(roleEntity);

        Assertions.assertEquals(expectedRoleModel, actualRoleModel,
                "The mapping to Role is not correct.");
    }
}
