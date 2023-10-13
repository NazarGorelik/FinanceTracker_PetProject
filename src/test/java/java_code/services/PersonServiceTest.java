package java_code.services;


import java_code.dto.admin.AdminPersonDTO;
import java_code.dto.user.AccountDTO;
import java_code.dto.user.PersonDTO;
import java_code.mappers.PersonMapper;
import java_code.models.Account;
import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.utilClassesForService.PersonServiceUtil;
import java_code.utilClassesForTesting.Initializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Autowired
    private PersonService personService;
    @Autowired
    private Initializer initializer;
    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private PersonServiceUtil personServiceUtil;
    @MockBean
    private PersonMapper personMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private Person person;

    @BeforeEach
    void setUp() {
        person = initializer.initializePerson(1, "Jack", "123", Collections.emptyList());
    }

    @Test
    void findAdminPersonDTOByUsernameTest() {
        when(personServiceUtil.findById(person.getId())).thenReturn(person);
        when(personMapper.toAdminPersonDTO(person)).thenReturn(new AdminPersonDTO(person.getId(), person.getUsername(),
                person.getPassword(), person.getCreatedAt(), person.getRole(), null));

        AdminPersonDTO adminPersonDTO = personService.findAdminPersonDTOByUsername(person.getId());

        assertThat(adminPersonDTO).isNotNull();
        assertThat(adminPersonDTO.id()).isGreaterThan(0);
        assertThat(adminPersonDTO).isExactlyInstanceOf(AdminPersonDTO.class);
        verify(personServiceUtil, times(1)).findById(person.getId());
    }

    @Test
    void findAllAdminPersonDTOsTest() {
        Person person1 = initializer.initializePerson(2, "Bob", "bob228", Collections.emptyList());
        when(personRepository.findAll()).thenReturn(List.of(person, person1));

        List<AdminPersonDTO> adminPersonDTOS = personService.findAllAdminPersonDTOs();

        assertThat(adminPersonDTOS).isNotNull();
        assertThat(adminPersonDTOS.size()).isEqualTo(2);
        verify(personRepository, times(1)).findAll();
    }

    @Test
    void saveTest() {
        when(personRepository.save(person)).thenReturn(person);
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        PersonDTO personDTO = new PersonDTO(person.getUsername(), person.getPassword());
        personService.save(personDTO);
        Optional<Person> optionalPerson = personRepository.findById(person.getId());

        assertThat(optionalPerson.get().getId()).isEqualTo(person.getId());
        assertThat(optionalPerson.get().getUsername()).isEqualTo(person.getUsername());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void getAccountsById() {
        Account account1 = initializer.initializeAccount(1, "PayPal", 1000d, Collections.emptyList());
        Account account2 = initializer.initializeAccount(2, "Wallet", 500d, Collections.emptyList());
        List<Account> accountList = List.of(account1, account2);
        person.setAccounts(accountList);
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

        List<AccountDTO> accountDTOS = personService.getAccountsById(person.getId());

        assertThat(accountDTOS).isNotNull();
        assertThat(accountDTOS.size()).isEqualTo(accountList.size());
        assertThat(accountDTOS.get(0).name()).isEqualTo(accountList.get(0).getName());
        assertThat(accountDTOS.get(0).balance()).isEqualTo(accountList.get(0).getBalance());
        verify(personRepository, times(1)).findById(person.getId());
    }

    @Test
    void updateTest(){
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        PersonDTO personDTO = new PersonDTO("nazar", "nazar228");
        when(passwordEncoder.encode(any(String.class))).thenReturn(personDTO.password());

        personService.update(person.getId(), personDTO, "123");

        assertThat(person.getUsername()).isEqualTo(personDTO.username());
        assertThat(person.getPassword()).isEqualTo(personDTO.password());
        verify(personRepository, times(1)).findById(person.getId());
    }

    @Test
    void deleteTest(){
        when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);

        personService.delete(person.getId(), person.getPassword());

        verify(personRepository, times(1)).deleteById(person.getId());
    }
}