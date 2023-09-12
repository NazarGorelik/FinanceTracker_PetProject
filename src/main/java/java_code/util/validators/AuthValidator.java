package java_code.util.validators;

import java_code.dto.user.AuthenticationDTO;
import java_code.models.Person;
import java_code.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthValidator implements Validator {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return AuthenticationDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AuthenticationDTO authenticationDTO = (AuthenticationDTO) target;
        Optional<Person> optionalPerson = personRepository.findByUsername(authenticationDTO.username());
        boolean isPasswordSame = passwordEncoder.matches(authenticationDTO.password(), optionalPerson.get().getPassword());

        if(!optionalPerson.isPresent()) {
            errors.rejectValue("username","","Person with such username: " +
                    authenticationDTO.username() + " wasn't found");
        }else if(!isPasswordSame){
            errors.rejectValue("password", "", "Incorrect password");
        }
    }
}
