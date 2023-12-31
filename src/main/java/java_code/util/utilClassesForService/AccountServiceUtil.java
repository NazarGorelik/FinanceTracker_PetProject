package java_code.util.utilClassesForService;

import java_code.models.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountServiceUtil {

    private final PersonServiceUtil personServiceUtil;

    @Cacheable(cacheNames = "userAccounts", key="#accountName")
    public Optional<Account> findOptionalOfAccountInUserAccounts(String accountName, int userID) {
        List<Account> accounts = personServiceUtil.findById(userID).getAccounts();
        return accounts.stream().filter(account->account.getName().equals(accountName)).findAny();
    }
}
