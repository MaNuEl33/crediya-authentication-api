package co.com.crediya.model.user.gateways;

public interface PasswordEncrypter {
    String encrypt(String rawPassword);
}
