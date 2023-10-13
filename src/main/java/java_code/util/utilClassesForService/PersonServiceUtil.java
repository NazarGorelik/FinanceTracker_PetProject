package java_code.util.utilClassesForService;

import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.exceptions.businessLayer.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonServiceUtil {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public Person findById(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent())
            return optionalPerson.get();

        throw new PersonNotFoundException("Person with such id: " + id + " wasn't found");
    }
}
