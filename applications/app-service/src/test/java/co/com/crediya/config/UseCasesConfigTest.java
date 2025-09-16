package co.com.crediya.config;

import co.com.crediya.usecase.loginuser.LogInUserUseCase;
import co.com.crediya.usecase.registeruser.RegisterUserUseCase;
import co.com.crediya.usecase.searchusers.SearchUsersUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public RegisterUserUseCase registerUserUseCase() {
            return Mockito.mock(RegisterUserUseCase.class);
        }

        @Bean
        public LogInUserUseCase logInUserUseCase() {
            return Mockito.mock(LogInUserUseCase.class);
        }

        @Bean
        public SearchUsersUseCase  searchUsersUseCase() {
            return Mockito.mock(SearchUsersUseCase.class);
        }
    }
}