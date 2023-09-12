package java_code.util.exceptions;

public class AccountWithSuchNameAlreadyExistsException extends RuntimeException{
    public AccountWithSuchNameAlreadyExistsException(String msg){
        super(msg);
    }
}
