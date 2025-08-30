package co.com.crediya.bcryptpasswordencrypter;

import co.com.crediya.model.user.gateways.PasswordEncrypter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordEncrypterAdapter implements PasswordEncrypter {

    private final PasswordEncoder encoder;

    @Override
    public String encrypt(String rawPassword) {
        return encoder.encode(rawPassword);
    }
}
