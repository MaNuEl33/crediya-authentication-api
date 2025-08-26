package co.com.crediya.api.mappers;

import co.com.crediya.api.dtos.RegisterUserRequestDto;
import co.com.crediya.api.dtos.RegisterUserResponseDto;
import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

class UserDtoMapperTest {

    private final UserDtoMapper mapper = Mappers.getMapper(UserDtoMapper.class);

    @Test
    void shouldMapToUserModel() {
        final var requestDto = new RegisterUserRequestDto(
                "Julio", "Haro", LocalDate.of(1987, Month.JUNE, 12),
                "Address 1", "julioharo1987@gmail.com", "965212365",
                "43652125", BigDecimal.valueOf(4000), 1L
        );

        final var expectedUserModel = User.builder()
                .firstName("Julio")
                .lastName("Haro")
                .birthDate(LocalDate.of(1987, Month.JUNE, 12))
                .address("Address 1")
                .email("julioharo1987@gmail.com")
                .phoneNumber("965212365")
                .documentNumber("43652125")
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder().id(1L).build())
                .build();

        final var actualUserModel = this.mapper.toUserModel(requestDto);

        Assertions.assertEquals(expectedUserModel, actualUserModel,
                "The mapping to User is not correct.");
    }

    @Test
    void shouldMapToRegisterUserResponseDto() {
        final var user = User.builder()
                .id(1L)
                .firstName("Carlos")
                .lastName("Arroyo")
                .birthDate(LocalDate.of(1986, Month.JUNE, 8))
                .address("Address 2")
                .email("carlosarroyo1986@gmail.com")
                .phoneNumber("947586784")
                .documentNumber("43685768")
                .baseSalary(BigDecimal.valueOf(5000))
                .role(Role.builder().id(1L).build())
                .build();

        final var expectedRegisterUserResponseDto = new RegisterUserResponseDto(
                1L, "Carlos", "Arroyo", LocalDate.of(1986, Month.JUNE, 8),
                "Address 2", "carlosarroyo1986@gmail.com", "947586784",
                "43685768", BigDecimal.valueOf(5000), 1L
        );

        final var actualRegisterUserResponseDto = this.mapper.toRegisterUserResponseDto(user);

        Assertions.assertEquals(expectedRegisterUserResponseDto, actualRegisterUserResponseDto,
                "The mapping to RegisterUserResponseDto is not correct.");
    }
}
