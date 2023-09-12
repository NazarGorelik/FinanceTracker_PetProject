package java_code.services;

import java_code.dto.user.responses.AuthenticationResponse;
import java_code.security.JWTGenerator;
import java_code.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JWTGenerator jwtGenerator;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse attemptLogin(String username, String password) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                (username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var principal = (UserPrincipal) authentication.getPrincipal();

        //get roles as string
        var roles = principal.getAuthorities().stream()
                .map(x -> x.getAuthority())
                .collect(Collectors.toList());

        String token = jwtGenerator.generateToken(principal.getPerson().getId(), principal.getUsername(), roles);

        return AuthenticationResponse.builder()
                .accessToken(token)
                .build();
    }
}
