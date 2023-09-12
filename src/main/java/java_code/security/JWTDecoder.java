package java_code.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JWTDecoder {

    @Value("JWT-Secret")
    private String secret;

    //get data from token
    public DecodedJWT decode(String token){
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }
}
