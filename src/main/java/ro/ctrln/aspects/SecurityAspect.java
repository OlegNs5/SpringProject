package ro.ctrln.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ctrln.entities.User;
import ro.ctrln.enums.Roles;
import ro.ctrln.exceptions.InvalidCostumerIdException;
import ro.ctrln.exceptions.InvalidOperationException;
import ro.ctrln.repositories.UserRepository;

import java.util.Collection;
import java.util.Optional;

@Aspect
@Component
@Slf4j
public class SecurityAspect {

    @Autowired
    private UserRepository userRepository;

    @Pointcut("execution(* ro.ctrln.services.ProductService.addProduct(..))")
    public void addProductPointcut(){

    }
    @Pointcut("execution(* ro.ctrln.services.ProductService.updateProduct(..))")
    public void updateProductPointcut(){

    }

    @Pointcut("execution(* ro.ctrln.services.ProductService.deleteProduct(..))")
    public void deleteProductPointcut(){
    }

    @Before("ro.ctrln.aspects.SecurityAspect.addProductPointcut()")
    public void checkSecurityBeforeAddingProduct(JoinPoint joinPoint) throws InvalidCostumerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(costumerId);
        if (!userOptional.isPresent()){
            throw new InvalidCostumerIdException();
        }

        User user = userOptional.get();

        if (userIsNotAllowedToAddProduct(user.getRoles())){
            throw new InvalidOperationException("Costumer NOT allowed to add products!");
        }

        log.info("Costumer ID: {} will ADD the product! ",costumerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.updateProductPointcut()")
    public void checkSecurityBeforeUpdatingProduct(JoinPoint joinPoint) throws InvalidCostumerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(costumerId);
        if (!userOptional.isPresent()){
            throw new InvalidCostumerIdException();
        }

        User user = userOptional.get();

        if (userIsNotAllowedToUpdateProduct(user.getRoles())){
            throw new InvalidOperationException("Costumer NOT allowed to UPDATE products!");
        }

        log.info("Costumer ID: {} will UPDATE the product! ",costumerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.deleteProductPointcut()")
    public void checkSecurityBeforeDeletingProduct(JoinPoint joinPoint) throws InvalidCostumerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        Optional<User> userOptional = userRepository.findById(costumerId);
        if (!userOptional.isPresent()){
            throw new InvalidCostumerIdException();
        }

        User user = userOptional.get();

        if (userIsNotAllowedToDeleteProduct(user.getRoles())){
            throw new InvalidOperationException("Costumer NOT allowed to DELETE products!");
        }

        log.info("Costumer ID: {} will DELETE the product! ",costumerId);
    }


    private boolean userIsNotAllowedToAddProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN);
    }

    private boolean userIsNotAllowedToUpdateProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN) && !roles.contains(Roles.EDITOR) ; //&& este ala bun, daca contine unul din astea doi atunci e bine
    }


    private boolean userIsNotAllowedToDeleteProduct(Collection<Roles> roles) {
        return !roles.contains(Roles.ADMIN);

    }
}
