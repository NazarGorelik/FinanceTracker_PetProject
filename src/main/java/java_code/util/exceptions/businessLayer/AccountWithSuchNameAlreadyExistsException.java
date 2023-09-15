package java_code.util.exceptions.businessLayer;

public class AccountWithSuchNameAlreadyExistsException extends BusinessLayerException{
    public AccountWithSuchNameAlreadyExistsException(String msg){
        super(msg);
    }
}
