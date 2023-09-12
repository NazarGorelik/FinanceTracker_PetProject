package java_code.services;

import java_code.dto.user.TransactionDTO;
import java_code.mappers.TransactionMapper;
import java_code.models.Account;
import java_code.models.Transaction;
import java_code.repositories.TransactionRepository;
import java_code.util.exceptions.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;


    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public List<TransactionDTO> getTransactionsByAccountName(String accountName, String username) {
        Account account = accountService.findByNameInUserAccounts(accountName, username);
        return account.getTransactions().stream().
                map(x -> TransactionMapper.INSTANCE.toTransactionDTO(x))
                .collect(Collectors.toList());
    }

    @Transactional
    public void save(TransactionDTO transactionDTO, String username, String accountName) {
        if (accountService.getIndexOfAccountInUserAccounts(accountName, username) == -1)
            throw new AccountNotFoundException("Account with such name: " + accountName + " wasn't found");

        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionDTO);
        Account account = accountService.findByNameInUserAccounts(accountName, username);
        enrichTransaction(transaction, account);
        transactionRepository.save(transaction);
    }

    private void enrichTransaction(Transaction transaction, Account account) {
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setAccount(account);
        changeAccountBalance(account, transaction.getAmount());
    }

    private void changeAccountBalance(Account account, double amount) {
        account.setBalance(account.getBalance() + amount);
    }
}