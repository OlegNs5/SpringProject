package ro.ctrln.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ro.ctrln.exceptions.*;

@ControllerAdvice
public class OnlineOrderHandler {

    @ExceptionHandler(InvalidProductIdException.class)
    public ResponseEntity<String> handleInvalidProductIdException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID-ul unui produs nu este valid !");
    }
    @ExceptionHandler(InvalidQuantityException.class)
    public ResponseEntity<String> handleInvalidQuantityException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cantitatea unui produs trebuie sa fie pozitiva !");
    }
    @ExceptionHandler(NotEnoughStockException.class)
    public ResponseEntity<String> handleNotEnoughStockException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu exista stocul necesar pentru unul dintre produse !");
    }
    @ExceptionHandler(InvalidProductsException.class)
    public ResponseEntity<String> handleInvalidProductsException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comanda nu contine nici un produs !");
    }
    @ExceptionHandler(InvalidOrderIdException.class)
    public ResponseEntity<String> handleInvalidOrderIdException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nu a fost transmis un ID de comanda valid!");
    }
    @ExceptionHandler(OrderCanceledException.class)
    public ResponseEntity<String> handleOrderCanceledException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comanda a fost deja anulata!");
    }
    @ExceptionHandler(OrderDeliveredException.class)
    public ResponseEntity<String> handleOrderDeliveredException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comanda a fost deja livrata!");
    }
    @ExceptionHandler(OrderNotYetDeliveredException.class)
    public ResponseEntity<String> handleOrderNotYetDeliveredException(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Comanda inca nu a fost livrata!");
    }
}
