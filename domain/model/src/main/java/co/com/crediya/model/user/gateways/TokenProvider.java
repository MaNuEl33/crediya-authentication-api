package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;

public interface TokenProvider {
    String generateToken(User user);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    String getRoleFromToken(String token);
}
