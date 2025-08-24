package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.RegisterUserRequestDto;
import co.com.crediya.api.dtos.RegisterUserResponseDto;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserDtoMapper {

    @Mapping(target = "id",  ignore = true)
    @Mapping(target = "role", source = "roleId")
    User toUserModel(RegisterUserRequestDto requestDto);

    @Mapping(target = "roleId", source = "role.id")
    RegisterUserResponseDto toRegisterUserResponseDto(User user);

    @Mapping(target = "id", source = "roleId")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    Role toRole(Long roleId);
}
