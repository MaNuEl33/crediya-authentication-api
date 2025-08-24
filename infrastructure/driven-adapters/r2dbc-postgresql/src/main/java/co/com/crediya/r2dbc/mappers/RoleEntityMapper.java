package co.com.crediya.r2dbc.mappers;

import co.com.crediya.model.role.Role;
import co.com.crediya.r2dbc.entities.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleEntityMapper {
    Role toRoleModel(RoleEntity roleEntity);
}
