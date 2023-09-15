package java_code.util.exceptions.businessLayer;

public class PersonNotFoundException extends BusinessLayerException{
    public PersonNotFoundException(String msg){
        super(msg);
    }
}
