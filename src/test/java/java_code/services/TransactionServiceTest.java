package java_code.services;

import java_code.dto.user.TransactionDTO;
import java_code.models.Account;
import java_code.models.Person;
import java_code.models.Transaction;
import java_code.repositories.TransactionRepository;
import java_code.util.TransactionType;
import java_code.util.utilClassesForService.AccountServiceUtil;
import java_code.utilClassesForTesting.Initializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.CacheEvict;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private Initializer initializer;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private AccountServiceUtil accountServiceUtil;

    Account account;
    Person person;
    Transaction transaction;
    List<Transaction> list;

    @BeforeEach
    public void setUp() {
        transaction = initializer.initializeTransaction(1, TransactionType.INCOME, "Salary", 1000d);
        Transaction transaction1 = initializer.initializeTransaction(2, TransactionType.EXPENSE, "Products", 200d);
        list = List.of(transaction, transaction1);
        account = initializer.initializeAccount(1, "PayPal", 1000d,list);
        person = initializer.initializePerson(1, "Jack", "123", Collections.singletonList(account));
    }

    @Test
    void getTransactionsByAccountNameTest() {
        when(accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), person.getId()))
                .thenReturn(Optional.of(account));

        List<TransactionDTO> transactionDTOS = transactionService.getTransactionsByAccountName(account.getName(), person.getId());

        assertThat(transactionDTOS).isNotNull();
        assertThat(list.get(0).getType()).isEqualTo(transactionDTOS.get(0).type());
        assertThat(list.get(1).getAmount()).isEqualTo(transactionDTOS.get(1).amount());
    }

    @Test
    void saveTest() {
        when(accountServiceUtil.findOptionalOfAccountInUserAccounts(account.getName(), person.getId()))
                .thenReturn(Optional.of(account));
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        transactionService.save(new TransactionDTO(transaction.getType(), transaction.getDescription(), transaction.getAmount()),
                person.getId(), account.getName());
        Transaction savedTransaction = transactionRepository.findById(transaction.getId()).get();

        assertThat(savedTransaction).isNotNull();
        assertThat(transaction.getType()).isEqualTo(savedTransaction.getType());
        assertThat(transaction.getAmount()).isEqualTo(savedTransaction.getAmount());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
}
