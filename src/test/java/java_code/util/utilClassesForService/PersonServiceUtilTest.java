package java_code.util.utilClassesForService;

import java_code.models.Person;
import java_code.repositories.PersonRepository;
import java_code.util.exceptions.businessLayer.PersonNotFoundException;
import java_code.utilClassesForTesting.Initializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PersonServiceUtilTest {

    @Autowired
    private PersonServiceUtil personServiceUtil;
    @Autowired
    private Initializer initializer;

    @MockBean
    private PersonRepository personRepository;
    @MockBean
    private Person person;

    @BeforeEach
    void setUp() {
        person = initializer.initializePerson(1, "Jack", "123", Collections.emptyList());
    }
    @Test
    void findByUsernameSuccessTest() {
        when(personRepository.findByUsername(person.getUsername())).thenReturn(Optional.of(person));
        Person foundPerson = personServiceUtil.findByUsername(person.getUsername());

        assertThat(foundPerson).isNotNull();
        assertThat(foundPerson.getId()).isGreaterThan(0);
        assertThat(person).isEqualTo(foundPerson);
        verify(personRepository, times(1)).findByUsername(person.getUsername());
    }

    @Test
    void findByUsernameFailureTest() {
        assertThatExceptionOfType(PersonNotFoundException.class)
                .isThrownBy(() -> personServiceUtil.findByUsername("fake name"));
        verify(personRepository, times(1)).findByUsername("fake name");
    }
}
