package java_code.util.exceptions;

public class TransactionNotCreatedException extends RuntimeException{
    public TransactionNotCreatedException(String msg){
        super(msg);
    }
}
