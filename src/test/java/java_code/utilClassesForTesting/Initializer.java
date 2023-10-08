package java_code.utilClassesForTesting;

import java_code.models.Account;
import java_code.models.Person;
import java_code.models.Transaction;
import java_code.util.TransactionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Initializer {
    public Person initializePerson(int id, String username, String password, List<Account> list){
        return Person.builder()
                .id(id)
                .username(username)
                .password(password)
                .accounts(list)
                .build();
    }
    public Account initializeAccount(int id, String name, double balance, List<Transaction> list){
        return Account.builder()
                .id(id)
                .name(name)
                .balance(balance)
                .transactions(list).build();
    }
    public Transaction initializeTransaction(int id, TransactionType type, String description, double amount){
        return Transaction.builder()
                .id(id)
                .type(String.valueOf(type))
                .description(description)
                .amount(amount).build();
    }
}
