package java_code.util.exceptions;

public class AccountNotCreatedException extends RuntimeException{
    public AccountNotCreatedException(String msg){
        super(msg);
    }
}
