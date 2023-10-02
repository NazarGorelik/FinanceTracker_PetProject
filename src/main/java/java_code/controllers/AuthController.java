package java_code.controllers;

import jakarta.validation.Valid;
import java_code.dto.user.AuthenticationDTO;
import java_code.dto.user.responses.AuthenticationResponse;
import java_code.dto.user.PersonDTO;
import java_code.services.AuthService;
import java_code.services.PersonService;
import java_code.util.ErrorUtil;
import java_code.util.exceptions.presentationLayer.InvalidCredentialsException;
import java_code.util.exceptions.presentationLayer.PersonNotCreatedException;
import java_code.util.validators.AuthValidator;
import java_code.util.validators.PersonValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PersonService personService;
    private final PersonValidator personValidator;
    private final AuthValidator authValidator;
    private final ErrorUtil errorUtil;


    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult){
        authValidator.validate(authenticationDTO, bindingResult);

        if(bindingResult.hasErrors())
            throw new InvalidCredentialsException(errorUtil.builtErrorResponse(bindingResult));

        return authService.attemptLogin(authenticationDTO.username(), authenticationDTO.password());
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        personValidator.validate(personDTO, bindingResult);
        if (bindingResult.hasErrors())
            throw new PersonNotCreatedException(errorUtil.builtErrorResponse(bindingResult));

        personService.save(personDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
