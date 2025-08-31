package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.LoginUserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LoginUserDtoMapper {
    LoginUserResponseDto toLoginUserResponseDto(String token);
}
