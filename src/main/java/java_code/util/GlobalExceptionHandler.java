package java_code.util;

import java_code.util.exceptions.businessLayer.BusinessLayerException;
import java_code.util.exceptions.presentationLayer.*;
import java_code.util.exceptions.repositoryLayer.RepositoryLayerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PresentationLayerException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BusinessLayerException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(RepositoryLayerException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(),
                LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
