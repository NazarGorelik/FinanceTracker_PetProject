package java_code.services;

import java_code.dto.admin.AdminAccountDTO;
import java_code.dto.user.AccountDTO;
import java_code.mappers.AccountMapper;
import java_code.models.Account;
import java_code.repositories.AccountRepository;
import java_code.util.exceptions.businessLayer.AccountWithSuchNameAlreadyExistsException;
import java_code.util.utilClassesForService.AccountServiceUtil;
import java_code.util.utilClassesForService.PersonServiceUtil;
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
    private final PersonServiceUtil personServiceUtil;
    private final AccountServiceUtil accountServiceUtil;


    private List<Account> findAll() {
        return accountRepository.findAll();
    }

    public List<AdminAccountDTO> findAllAdminAccountDTOs(){
        List<Account> accountList = findAll();
        List<AdminAccountDTO> adminAccountDTOS = accountList.stream().
                map(x->AccountMapper.INSTANCE.toAdminAccountDTO(x)).collect(Collectors.toList());
        return adminAccountDTOS;
    }

    @Transactional
    public void save(AccountDTO accountDTO, String username) {
        if (accountServiceUtil.findOptionalOfAccountInUserAccounts(accountDTO.name(), username).isPresent())
            throw new AccountWithSuchNameAlreadyExistsException("Account with such name: " + accountDTO.name() + " already exists");

        Account account = AccountMapper.INSTANCE.toAccount(accountDTO);
        enrichAccount(account, username);
        accountRepository.save(account);
    }

    private void enrichAccount(Account account, String username) {
        account.setCreatedAt(LocalDateTime.now());
        account.setOwner(personServiceUtil.findByUsername(username));
    }
}
