package java_code.mappers;

import java_code.dto.admin.AdminTransactionDTO;
import java_code.dto.user.TransactionDTO;
import java_code.models.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {

    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionDTO toTransactionDTO(Transaction transaction);

    @Mapping(source = "account.name", target = "accountName")
    AdminTransactionDTO toAdminTransactionDTO(Transaction transaction);

    //CONDITIONAL MAPPING WITH MAPSTRUCT
    @Mapping(target = "amount", expression = "java(mapAmount(transactionDTO))")
    Transaction toTransaction(TransactionDTO transactionDTO);

    default double mapAmount(TransactionDTO transactionDTO) {
        if (transactionDTO.type().equals("EXPENSE")) {
            return transactionDTO.amount() * (-1);
        }
        return transactionDTO.amount();
    }
}
