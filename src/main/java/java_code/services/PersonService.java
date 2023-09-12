package java_code.services;

import java_code.dto.user.AccountDTO;
import java_code.dto.user.PersonDTO;
import java_code.mappers.AccountMapper;
import java_code.mappers.PersonMapper;
import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.exceptions.PersonNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public Person findByUsername(String username) {
        Optional<Person> optionalPerson = personRepository.findByUsername(username);
        if (optionalPerson.isPresent())
            return optionalPerson.get();

        throw new PersonNotFoundException("Person with such name: " + username + " wasn't found");
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional
    public void save(PersonDTO personDTO) {
        Person person = PersonMapper.INSTANCE.toPerson(personDTO);
        enrichPerson(person);
        personRepository.save(person);
    }

    private void enrichPerson(Person person) {
        person.setCreated_at(LocalDateTime.now());
        person.setRole("ROLE_USER");
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
    }

    public List<AccountDTO> getAccountsById(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent())
            return optionalPerson.get().getAccounts().stream()
                    .map(x -> AccountMapper.INSTANCE.toAccountDTO(x))
                    .collect(Collectors.toList());

        throw new PersonNotFoundException("Person wasn't found");
    }
}
