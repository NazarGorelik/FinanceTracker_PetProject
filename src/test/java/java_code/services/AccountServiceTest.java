package java_code.services;

import java_code.dto.admin.AdminAccountDTO;
import java_code.dto.user.AccountDTO;
import java_code.models.Account;
import java_code.models.Person;
import java_code.repositories.AccountRepository;
import java_code.util.exceptions.businessLayer.AccountWithSuchNameAlreadyExistsException;
import java_code.util.utilClassesForService.AccountServiceUtil;
import java_code.util.utilClassesForService.PersonServiceUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java_code.utilClassesForTesting.Initializer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private Initializer initializer;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountServiceUtil accountServiceUtil;
    @MockBean
    private PersonServiceUtil personServiceUtil;

    Account account;

    @BeforeEach
    void setUp() {
        account = initializer.initializeAccount(1, "PayPal", 1000d, Collections.emptyList());
    }

    @Test
    void findAllAdminAccountDTOsTest() {
        Account account1 = initializer.initializeAccount(2, "BankAccount", 500d, Collections.emptyList());
        List<Account> list = List.of(account, account1);
        when(accountRepository.findAll()).thenReturn(list);

        List<AdminAccountDTO> adminAccountDTOS = accountService.findAllAdminAccountDTOs();
        assertThat(adminAccountDTOS).isNotNull();
        assertThat(adminAccountDTOS.size()).isEqualTo(list.size());
        assertThat(adminAccountDTOS.get(0).name()).isEqualTo(list.get(0).getName());
        assertThat(adminAccountDTOS.get(1).balance()).isEqualTo(list.get(1).getBalance());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void saveSuccessTest() {
        Person person = initializer.initializePerson(1, "Jack", "123", Collections.emptyList());
        when(accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), person.getId()))
                .thenReturn(Optional.empty());
        when(personServiceUtil.findById(person.getId())).thenReturn(person);
        when(accountRepository.findById(any(Integer.class))).thenReturn(Optional.of(account));

        AccountDTO accountDTO = new AccountDTO(account.getBalance(), account.getName());
        accountService.save(accountDTO, person.getId());
        Account savedAccount = accountRepository.findById(1).get();

        assertThat(savedAccount).isEqualTo(account);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void saveFailureTest() {
        Person person = initializer.initializePerson(1, "Jack", "123", Collections.emptyList());
        when(accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), person.getId()))
                .thenReturn(Optional.of(account));
        when(personServiceUtil.findById(person.getId())).thenReturn(person);

        AccountDTO accountDTO = new AccountDTO(account.getBalance(), account.getName());

        assertThatExceptionOfType(AccountWithSuchNameAlreadyExistsException.class)
                .isThrownBy(() -> accountService.save(accountDTO, person.getId()));
        verify(accountServiceUtil, times(1))
                .findOptionalOfAccountInUserAccounts(account.getName(), person.getId());
    }

    @Test
    void updateTest(){
        when(accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), 1))
                .thenReturn(Optional.of(account));

        AccountDTO accountDTO = new AccountDTO(666d, "Wallet");
        accountService.update(1, accountDTO, account.getName());

        assertThat(account.getName()).isEqualTo(accountDTO.name());
        assertThat(account.getBalance()).isEqualTo(accountDTO.balance());
    }

    @Test
    void deleteTest(){
        when(accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), 1))
                .thenReturn(Optional.of(account));

        accountService.delete(1, account.getName());

        verify(accountRepository, times(1)).deleteById(account.getId());
    }
}
