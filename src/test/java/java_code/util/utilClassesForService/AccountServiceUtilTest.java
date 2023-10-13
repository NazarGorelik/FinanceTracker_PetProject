package java_code.util.utilClassesForService;

import java_code.models.Account;
import java_code.models.Person;
import java_code.utilClassesForTesting.Initializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceUtilTest {

    @Autowired
    private AccountServiceUtil accountServiceUtil;
    @Autowired
    private Initializer initializer;
    @MockBean
    private PersonServiceUtil personServiceUtil;

    private Person person;
    private Account account;

    @BeforeEach
    void setUp() {
        account = initializer.initializeAccount(1, "PayPal", 1000d, Collections.emptyList());
        person = initializer.initializePerson(1, "Jack", "123", List.of(account));
    }

    @Test
    public void findOptionalOfAccountInUserAccountsTest() {
        when(personServiceUtil.findById(person.getId())).thenReturn(person);

        Account userAccount = accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), person.getId()).get();
        assertThat(userAccount).isNotNull();
        assertThat(userAccount.getId()).isEqualTo(account.getId());
        assertThat(userAccount.getName()).isEqualTo(account.getName());
    }
}
