package co.com.crediya.r2dbc.mappers;

import co.com.crediya.model.role.Role;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entities.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

class UserEntityMapperTest {

    private final UserEntityMapper mapper = Mappers.getMapper(UserEntityMapper.class);

    @Test
    void shouldMapToUserEntity() {
        final var user = User.builder()
                .id(1L)
                .firstName("Carlos")
                .lastName("Haro")
                .birthDate(LocalDate.of(2025, Month.AUGUST, 25))
                .address("Address 1")
                .email("carlosharo1986@outlook.com")
                .password("mi_poderoso_password_1")
                .phoneNumber("986767878")
                .documentNumber("43689015")
                .baseSalary(BigDecimal.valueOf(4000))
                .role(Role.builder().id(1L).build())
                .build();

        final var expectedUserEntity = UserEntity.builder()
                .id(1L)
                .firstName("Carlos")
                .lastName("Haro")
                .birthDate(LocalDate.of(2025, Month.AUGUST, 25))
                .address("Address 1")
                .email("carlosharo1986@outlook.com")
                .password("mi_poderoso_password_1")
                .phoneNumber("986767878")
                .documentNumber("43689015")
                .baseSalary(BigDecimal.valueOf(4000))
                .roleId(1L)
                .build();

        final var actualUserEntity = this.mapper.toUserEntity(user);

        Assertions.assertEquals(expectedUserEntity, actualUserEntity,
                "The mapping to UserEntity is not correct.");
    }

    @Test
    void shouldMapToUserModel() {
        final var userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Julio")
                .lastName("Arroyo")
                .birthDate(LocalDate.of(2025, Month.AUGUST, 25))
                .address("Address 2")
                .email("julioarroyo1987@outlook.com")
                .password("mi_poderoso_password_2")
                .phoneNumber("986743878")
                .documentNumber("43682315")
                .baseSalary(BigDecimal.valueOf(5000))
                .roleId(1L)
                .build();

        final var expectedUserModel = User.builder()
                .id(1L)
                .firstName("Julio")
                .lastName("Arroyo")
                .birthDate(LocalDate.of(2025, Month.AUGUST, 25))
                .address("Address 2")
                .email("julioarroyo1987@outlook.com")
                .password("mi_poderoso_password_2")
                .phoneNumber("986743878")
                .documentNumber("43682315")
                .baseSalary(BigDecimal.valueOf(5000))
                .role(Role.builder().id(1L).build())
                .build();

        final var actualUserModel = this.mapper.toUserModel(userEntity);

        Assertions.assertEquals(expectedUserModel, actualUserModel,
                "The mapping to User is not correct.");
    }
}
