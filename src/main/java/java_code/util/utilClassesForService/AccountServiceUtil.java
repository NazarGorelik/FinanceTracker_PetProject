package java_code.util.utilClassesForService;

import java_code.models.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountServiceUtil {

    private final PersonServiceUtil personServiceUtil;

    public Optional<Account> findOptionalOfAccountInUserAccounts(String accountName, String username) {
        List<Account> accounts = personServiceUtil.findByUsername(username).getAccounts();
        return accounts.stream().filter(account->account.getName().equals(accountName)).findAny();
    }
}
