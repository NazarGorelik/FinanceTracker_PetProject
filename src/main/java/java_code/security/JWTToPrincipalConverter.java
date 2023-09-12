package java_code.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import java_code.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class JWTToPrincipalConverter {

    private final PersonRepository personRepository;

    public UserPrincipal convert(DecodedJWT decodedJWT) {
        return new UserPrincipal(personRepository.findByUsername(decodedJWT.getClaim("username").asString()).get());
    }

    //FOR FUTURE IF USER HAS MANY ROLES
//    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT decodedJWT) {
//        var claim = decodedJWT.getClaim("roles");
//        if (claim.isNull() || claim.isMissing()) return List.of();
//        return claim.asList(SimpleGrantedAuthority.class);
//    }
}
