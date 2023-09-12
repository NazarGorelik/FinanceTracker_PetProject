package java_code.mappers;

import java_code.dto.admin.AdminAccountDTO;
import java_code.dto.admin.AdminTransactionDTO;
import java_code.dto.user.AccountDTO;
import java_code.models.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper( AccountMapper.class );

    Account toAccount(AccountDTO accountDTO);
    AccountDTO toAccountDTO(Account account);

    @Mapping(source = "owner.username", target = "owner")
    @Mapping(target = "transactions", expression = "java(mapTransactions(account))")
    AdminAccountDTO toAdminAccountDTO(Account account);

    default List<AdminTransactionDTO> mapTransactions(Account account){
        return account.getTransactions().stream().map(x->TransactionMapper.INSTANCE.toAdminTransactionDTO(x))
                .collect(Collectors.toList());
    }
}
