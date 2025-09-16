package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.SearchUsersResponseDto;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SearchUsersDtoMapper {

    @Mapping(target = "roleId", source = "role.id")
    SearchUsersResponseDto toSearchUsersResponseDto(User user);
}
