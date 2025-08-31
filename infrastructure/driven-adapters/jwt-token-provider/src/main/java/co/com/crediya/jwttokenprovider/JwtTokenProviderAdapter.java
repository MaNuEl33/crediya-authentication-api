package co.com.crediya.jwttokenprovider;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.TokenProvider;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import javax.crypto.SecretKey;
import java.util.Date;

@RequiredArgsConstructor
@Log4j2
public class JwtTokenProviderAdapter implements TokenProvider {

    private static final String ROLE_CLAIM_NAME = "role";

    private final String secret;
    private final Integer expiration;

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim(ROLE_CLAIM_NAME, user.getRole().getName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + this.expiration * 1000))
                .signWith(this.getKey(this.secret))
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(this.getKey(this.secret)).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("An error occurred while validating the token.", e);
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token) {
        return Jwts.parser().verifyWith(this.getKey(this.secret)).build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }

    @Override
    public String getRoleFromToken(String token) {
        return Jwts.parser().verifyWith(this.getKey(this.secret)).build()
                .parseSignedClaims(token)
                .getPayload().get(ROLE_CLAIM_NAME, String.class);
    }

    private SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}
