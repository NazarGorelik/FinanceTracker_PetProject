package java_code.util.utilClassesForService;

import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.exceptions.businessLayer.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PersonServiceUtil {

    private final PersonRepository personRepository;

    public Person findByUsername(String username) {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson.isPresent())
            return optionalPerson.get();

        throw new PersonNotFoundException("Person with such name: " + username + " wasn't found");
    }
}
