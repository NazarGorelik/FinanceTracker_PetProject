package java_code.services;

import java_code.dto.user.AccountDTO;
import java_code.mappers.AccountMapper;
import java_code.models.Account;
import java_code.repositories.AccountRepository;
import java_code.util.exceptions.AccountNotFoundException;
import java_code.util.exceptions.AccountWithSuchNameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PersonService personService;


    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    public Account findByNameInUserAccounts(String accountName, String username) {
        List<Account> accounts = personService.findByUsername(username).getAccounts();
        int index = getIndexOfAccountInUserAccounts(accountName, username);
        if (index == -1)
            throw new AccountNotFoundException("Account with such name: " + accountName + " wasn't found");
        return accounts.get(index);
    }

    public int getIndexOfAccountInUserAccounts(String accountName, String username) {
        List<Account> accounts = personService.findByUsername(username).getAccounts();
        List<String> accountsNames = accounts.stream().map(x -> x.getName()).collect(Collectors.toList());
        if (accountsNames.contains(accountName))
            return accountsNames.indexOf(accountName);
        return -1;
    }

    @Transactional
    public void save(AccountDTO accountDTO, String username) {
        if (getIndexOfAccountInUserAccounts(accountDTO.name(), username) != -1)
            throw new AccountWithSuchNameAlreadyExistsException("Account with such name: " + accountDTO.name() + " already exists");
        Account account = AccountMapper.INSTANCE.toAccount(accountDTO);
        enrichAccount(account, username);
        accountRepository.save(account);
    }

    private void enrichAccount(Account account, String username) {
        account.setCreated_at(LocalDateTime.now());
        account.setOwner(personService.findByUsername(username));
    }
}
