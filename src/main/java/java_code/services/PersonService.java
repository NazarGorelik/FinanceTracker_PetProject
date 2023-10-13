package java_code.services;

import java_code.dto.admin.AdminPersonDTO;
import java_code.dto.user.AccountDTO;
import java_code.dto.user.PersonDTO;
import java_code.mappers.AccountMapper;
import java_code.mappers.PersonMapper;
import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.exceptions.businessLayer.BusinessLayerException;
import java_code.util.exceptions.businessLayer.InvalidPasswordException;
import java_code.util.exceptions.businessLayer.PersonNotFoundException;
import java_code.util.utilClassesForService.PersonServiceUtil;
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
    private final PersonServiceUtil personServiceUtil;


    public AdminPersonDTO findAdminPersonDTOByUsername(int userID) {
        Person person = personServiceUtil.findById(userID);
        return PersonMapper.INSTANCE.toAdminPersonDTO(person);
    }

    public List<AdminPersonDTO> findAllAdminPersonDTOs() {
        List<Person> personList = findAll();
        List<AdminPersonDTO> adminPersonDTOS = personList.stream().
                map(x -> PersonMapper.INSTANCE.toAdminPersonDTO(x)).collect(Collectors.toList());
        return adminPersonDTOS;
    }

    private List<Person> findAll() {
        return personRepository.findAll();
    }

    @Transactional
    public void save(PersonDTO personDTO) {
        Person person = PersonMapper.INSTANCE.toPerson(personDTO);
        enrichPerson(person);
        personRepository.save(person);
    }

    private void enrichPerson(Person person) {
        person.setCreatedAt(LocalDateTime.now());
        person.setRole("ROLE_USER");
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
    }

    public List<AccountDTO> getAccountsById(int userID) {
        Person person = findPersonOrThrowException(userID,
                new PersonNotFoundException("Person with such id: " + userID + " wasn't found"));

        return person.getAccounts().stream()
                .map(x -> AccountMapper.INSTANCE.toAccountDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public void update(int userID, PersonDTO updatedPerson, String password) {
        Person savedPerson = findPersonAndCheckPassword(userID, password);

        savedPerson.setUsername(updatedPerson.username());
        savedPerson.setPassword(passwordEncoder.encode(updatedPerson.password()));
    }

    @Transactional
    public void delete(int userID, String password) {
        findPersonAndCheckPassword(userID, password);
        personRepository.deleteById(userID);
    }

    private Person findPersonAndCheckPassword(int userID, String password) {
        Person savedPerson = findPersonOrThrowException(userID,
                new PersonNotFoundException("Person with such id: " + userID + " wasn't found"));

        boolean arePasswordSame = passwordEncoder.matches(password, savedPerson.getPassword());
        if (!arePasswordSame)
            throw new InvalidPasswordException("Password does not match. Please try again");

        return savedPerson;
    }

    private Person findPersonOrThrowException(int userID, BusinessLayerException exception) {
        Optional<Person> optionalPerson = personRepository.findById(userID);
        if (!optionalPerson.isPresent())
            throw exception;

        return optionalPerson.get();
    }
}
