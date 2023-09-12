package java_code.util.validators;

import java_code.dto.user.TransactionDTO;
import java_code.repositories.AccountRepository;
import java_code.util.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class TransactionValidator implements Validator {

    private final AccountRepository accountRepository;


    @Override
    public boolean supports(Class<?> clazz) {
        return TransactionDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransactionDTO transactionDTO = (TransactionDTO) target;

        try {
            TransactionType.valueOf(transactionDTO.type());
        } catch (IllegalArgumentException e) {
            errors.rejectValue("type", "", "Incorrect transaction type. Transaction must be " +
                    "EXPENSE or INCOME");
        }
    }
}
