package java_code.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Component
public class JWTGenerator {

    @Value("JWT-Secret")
    private String secret;

    private Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(60).toInstant());

    public String generateToken(int id, String username, List<String> roles) {
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(expirationDate)
                .withClaim("username", username)
                .withClaim("roles", roles)
                .sign(Algorithm.HMAC256(secret));
    }
}
