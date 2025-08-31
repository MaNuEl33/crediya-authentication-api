package co.com.crediya.jwttokenprovider.config;

import co.com.crediya.jwttokenprovider.JwtTokenProviderAdapter;
import co.com.crediya.model.user.gateways.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Objects;

@Configuration
public class JwtConfig {

    @Bean
    public TokenProvider tokenProvider(Environment environment) {
        final var secret = environment.getProperty("jwt.secret");
        final var expiration = Integer.parseInt(Objects.requireNonNull(environment.getProperty("jwt.expiration")));

        return new JwtTokenProviderAdapter(secret, expiration);
    }
}
