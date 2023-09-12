package java_code.util.validators;

import java_code.dto.user.PersonDTO;
import java_code.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class PersonValidator implements Validator {

    private final PersonRepository personRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return PersonDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;

        if (personRepository.findByUsername(personDTO.username()).isPresent()) {
            errors.rejectValue("username", "", "Username is already taken");
        }
    }
}
