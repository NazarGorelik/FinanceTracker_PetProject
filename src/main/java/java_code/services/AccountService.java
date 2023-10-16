package java_code.services;

import java_code.dto.admin.AdminAccountDTO;
import java_code.dto.user.AccountDTO;
import java_code.mappers.AccountMapper;
import java_code.models.Account;
import java_code.repositories.AccountRepository;
import java_code.util.exceptions.businessLayer.AccountNotFoundException;
import java_code.util.exceptions.businessLayer.AccountWithSuchNameAlreadyExistsException;
import java_code.util.exceptions.businessLayer.BusinessLayerException;
import java_code.util.utilClassesForService.AccountServiceUtil;
import java_code.util.utilClassesForService.PersonServiceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PersonServiceUtil personServiceUtil;
    private final AccountServiceUtil accountServiceUtil;


    public List<AdminAccountDTO> findAllAdminAccountDTOs() {
        List<Account> accountList = findAll();
        List<AdminAccountDTO> adminAccountDTOS = accountList.stream().
                map(x -> AccountMapper.INSTANCE.toAdminAccountDTO(x)).collect(Collectors.toList());
        return adminAccountDTOS;
    }

    private List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Cacheable(cacheNames = "accounts", key = "#accountDTO.name()")
    @Transactional
    public void save(AccountDTO accountDTO, int userID) {
        Optional<Account> optionalAccount = accountServiceUtil.findOptionalOfAccountInUserAccounts(accountDTO.name(), userID);
        if (optionalAccount.isPresent())
            throw new AccountWithSuchNameAlreadyExistsException("Account with such name: " + accountDTO.name() + " already exists");

        Account account = AccountMapper.INSTANCE.toAccount(accountDTO);
        enrichAccount(account, userID);
        accountRepository.save(account);
    }

    private void enrichAccount(Account account, int userID) {
        account.setCreatedAt(LocalDateTime.now());
        account.setOwner(personServiceUtil.findById(userID));
    }

    @CachePut(cacheNames = "accounts", key = "#accountName")
    @Transactional
    public void update(int userID, AccountDTO updatedAccount, String accountName) {
        Account account = findAccountInUserAccountsOrThrowException(accountName, userID,
                new AccountNotFoundException("Account with such name " + accountName + " wasn't found"));

        account.setName(updatedAccount.name());
        account.setBalance(updatedAccount.balance());
    }

    @CacheEvict(cacheNames = "accounts", key = "#accountName")
    @Transactional
    public void delete(int userID, String accountName) {
        Account account = findAccountInUserAccountsOrThrowException(accountName, userID,
                new AccountNotFoundException("Account with such name " + accountName + " wasn't found"));

        accountRepository.deleteById(account.getId());
    }

    private Account findAccountInUserAccountsOrThrowException(String accountName, int userID, BusinessLayerException exception){
        Optional<Account> optionalAccount = accountServiceUtil.findOptionalOfAccountInUserAccounts(accountName, userID);
        if (!optionalAccount.isPresent())
            throw exception;

        return optionalAccount.get();
    }
}
