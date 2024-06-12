package ro.ctrln.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.ctrln.exceptions.InvalidCostumerIdException;
import ro.ctrln.exceptions.InvalidOperationException;

@ControllerAdvice
public class SecurityHandler {

    @ExceptionHandler(InvalidCostumerIdException.class)
    public ResponseEntity<String> handlerInvalidCostumerIdException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Costumer Id is invalid!");
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<String> handleInvalidOperationException(InvalidOperationException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}
