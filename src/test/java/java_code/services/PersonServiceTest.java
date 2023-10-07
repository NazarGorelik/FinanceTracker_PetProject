package java_code.services;


import java_code.dto.admin.AdminPersonDTO;
import java_code.dto.user.AccountDTO;
import java_code.dto.user.PersonDTO;
import java_code.mappers.PersonMapper;
import java_code.models.Account;
import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.exceptions.businessLayer.PersonNotFoundException;
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

    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PersonService personService;
    @MockBean
    private PersonMapper personMapper;

    @MockBean
    private Person person;

    @BeforeEach
    void setUp() {
        person = Person.builder()
                .id(1)
                .username("jack")
                .password("123")
                .accounts(Collections.emptyList())
                .build();
    }

    @Test
    void findByUsernameSuccessTest() {
        when(personRepository.findByUsername(person.getUsername())).thenReturn(Optional.of(person));
        Person foundPerson = personService.findByUsername(person.getUsername());

        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getId()).isGreaterThan(0);
        assertThat(person).isEqualTo(foundPerson);
        verify(personRepository, times(1)).findByUsername(person.getUsername());
    }

    @Test
    void findByUsernameFailureTest() {
        assertThatExceptionOfType(PersonNotFoundException.class)
                .isThrownBy(() -> personService.findByUsername("fake name"));
        verify(personRepository, times(1)).findByUsername("fake name");
    }

    @Test
    void findAdminPersonDTOByUsernameTest() {
        when(personRepository.findByUsername(person.getUsername())).thenReturn(Optional.of(person));
        when(personMapper.toAdminPersonDTO(person)).thenReturn(new AdminPersonDTO(person.getId(), person.getUsername(),
                person.getPassword(), person.getCreatedAt(), person.getRole(), null));

        AdminPersonDTO adminPersonDTO = personService.findAdminPersonDTOByUsername(person.getUsername());

        assertThat(adminPersonDTO).isNotNull();
        assertThat(adminPersonDTO.id()).isGreaterThan(0);
        assertThat(adminPersonDTO).isExactlyInstanceOf(AdminPersonDTO.class);
        verify(personRepository, times(1)).findByUsername(person.getUsername());
    }

    @Test
    void findAllAdminPersonDTOsTest() {
        Person person1 = Person.builder()
                .id(2)
                .username("bob")
                .password("bob228")
                .accounts(Collections.emptyList())
                .build();
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
        when(passwordEncoder.encode(person.getPassword())).thenReturn("123");

        PersonDTO personDTO = new PersonDTO(person.getUsername(), person.getPassword());
        personService.save(personDTO);
        Optional<Person> optionalPerson = personRepository.findById(person.getId());

        assertThat(optionalPerson.get().getId()).isEqualTo(person.getId());
        assertThat(optionalPerson.get().getPassword()).isEqualTo(person.getPassword());
        assertThat(optionalPerson.get().getUsername()).isEqualTo(person.getUsername());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void getAccountsById() {
        Account account1 = Account.builder()
                .name("PayPal")
                .balance(1000d)
                .build();
        Account account2 = Account.builder()
                .name("Wallet")
                .balance(50d)
                .build();
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
}