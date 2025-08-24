package co.com.crediya.r2dbc.mappers;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEntityMapper {

    @Mapping(target = "roleId", source = "role.id")
    UserEntity toUserEntity(User user);

    @Mapping(target = "role", source = "roleId")
    User toUserModel(UserEntity userEntity);

    @Mapping(target = "id", source = "roleId")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    Role toRole(Long roleId);
}
