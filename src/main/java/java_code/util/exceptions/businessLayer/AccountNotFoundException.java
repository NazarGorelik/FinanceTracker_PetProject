package java_code.util.exceptions.businessLayer;

public class AccountNotFoundException extends BusinessLayerException{
    public AccountNotFoundException(String msg){
        super(msg);
    }
}
