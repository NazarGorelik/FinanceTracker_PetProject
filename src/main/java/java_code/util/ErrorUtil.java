package java_code.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ErrorUtil {

    public String builtErrorResponse(BindingResult bindingResult){
        StringBuilder errorMessage = new StringBuilder();
        if (bindingResult.hasErrors()) {
            List<FieldError> list = bindingResult.getFieldErrors();
            for (FieldError e : list) {
                errorMessage.append(e.getField())
                        .append(" - ").append(e.getDefaultMessage())
                        .append(";");
            }
        }
        return errorMessage.toString();
    }
}
