package ro.ctrln.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ro.ctrln.entities.User;
import ro.ctrln.enums.Operations;
import ro.ctrln.enums.Roles;
import ro.ctrln.exceptions.InvalidCustomerIdException;
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

    @Pointcut("execution(* ro.ctrln.services.ProductService.addStock(..))")
    public void addStockPointcut(){
    }

    @Pointcut("execution(* ro.ctrln.services.OrderService.addOrder(..))")
    public void addOrderPointcut(){
    }
    @Pointcut("execution(* ro.ctrln.services.OrderService.deliverOrder(..))")
    public void deliverOrderPointcut(){
    }
    @Pointcut("execution(* ro.ctrln.services.OrderService.cancelOrder(..))")
    public void cancelOrderPointcut (){
    }
    @Pointcut("execution(* ro.ctrln.services.OrderService.returnOrder(..))")
    public void returnOrderPointcut (){
    }



    @Before("ro.ctrln.aspects.SecurityAspect.addProductPointcut()")
    public void checkSecurityBeforeAddingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.ADD_PRODUCT)){
            throw new InvalidOperationException("Customer NOT allowed to add products!");
        }

        log.info("Customer ID: {} will ADD the product! ",costumerId);
    }

    @Before("ro.ctrln.aspects.SecurityAspect.updateProductPointcut()")
    public void checkSecurityBeforeUpdatingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.UPDATE_PRODUCT)){
            throw new InvalidOperationException("Customer NOT allowed to UPDATE products!");
        }

        log.info("Customer ID: {} will UPDATE the product! ",costumerId);
    }
    @Before("ro.ctrln.aspects.SecurityAspect.addOrderPointcut()")
    public void checkSecurityBeforeAddingAnOrder(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.ADD_ORDER)){
            throw new InvalidOperationException("Customer NOT allowed to Add an ORDER!");
        }

        log.info("Customer ID: {} will Add an ORDER! ",costumerId);
    }



    @Before("ro.ctrln.aspects.SecurityAspect.deleteProductPointcut()")
    public void checkSecurityBeforeDeletingProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.DELETE_PRODUCT)){
            throw new InvalidOperationException("Customer NOT allowed to DELETE products!");
        }

        log.info("Customer ID: {} will DELETE the product! ",costumerId);
    }
    @Before("ro.ctrln.aspects.SecurityAspect.deliverOrderPointcut()")
    public void checkSecurityBeforeDeliveringAnOrder(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.DELIVER_ORDER)){
            throw new InvalidOperationException("Customer NOT allowed to DELIVER an ORDER!");
        }

        log.info("Customer ID: {} will DELIVER an ORDER! ",costumerId);
    }
    @Before("ro.ctrln.aspects.SecurityAspect.returnOrderPointcut()")
    public void checkSecurityBeforeReturningAnOrder(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.RETURN_ORDER)){
            throw new InvalidOperationException("Customer NOT allowed to RETURN an ORDER!");
        }

        log.info("Customer ID: {} will RETURN an ORDER! ",costumerId);

    }
    @Before("ro.ctrln.aspects.SecurityAspect.cancelOrderPointcut()") ///------
    public void checkSecurityBeforeCancelingAnOrder(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[1
                ];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.CANCEL_ORDER)){
            throw new InvalidOperationException("Customer NOT allowed to CANCEL an order!");
        }

        log.info("Customer ID: {} will CANCEL an ORDER! ",costumerId);

    }
    @Before("ro.ctrln.aspects.SecurityAspect.addStockPointcut()")
    public void checkSecurityBeforeAddingStockForProduct(JoinPoint joinPoint) throws InvalidCustomerIdException, InvalidOperationException {
        Long costumerId = (Long) joinPoint.getArgs()[2];
        User user = getUser(costumerId);

        if (userIsNotAllowedOperation(user.getRoles(),Operations.ADD_STOCK)){
            throw new InvalidOperationException("Customer NOT allowed to ADD STOCK products!");
        }

        log.info("Customer ID: {} will ADD STOCK the product! ",costumerId);
    }

    private User getUser(Long costumerId) throws InvalidCustomerIdException {
        Optional<User> userOptional = userRepository.findById(costumerId);
        if (!userOptional.isPresent()){
            throw new InvalidCustomerIdException();
        }
        return userOptional.get();
    }

    private boolean userIsNotAllowedOperation(Collection<Roles> roles, Operations operation) {
        switch (operation){
            case ADD_PRODUCT:
            case DELETE_PRODUCT:
            case ADD_STOCK:
                return !roles.contains(Roles.ADMIN);
            case UPDATE_PRODUCT:
                return !roles.contains(Roles.ADMIN) && !roles.contains(Roles.EDITOR) ; //&& este ala bun, daca contine unul din astea doi atunci e bine
            case ADD_ORDER:
            case CANCEL_ORDER:
            case RETURN_ORDER:
                return !roles.contains(Roles.CLIENT);
            case DELIVER_ORDER:
                return !roles.contains(Roles.EXPEDITOR);
        }
        return false;
    }

//    private boolean userIsNotAllowedToAddStockForProduct(Collection<Roles> roles) {
//        return userIsNotAllowedOperation(roles,Operations.ADD_STOCK);
//        return !roles.contains(Roles.ADMIN);
//    }
//
//    private boolean userIsNotAllowedToAddProduct(Collection<Roles> roles) {
//        return userIsNotAllowedOperation(roles,Operations.ADD_PRODUCT);
//        return !roles.contains(Roles.ADMIN);
//    }
//
//    private boolean userIsNotAllowedToUpdateProduct(Collection<Roles> roles) {
//        return userIsNotAllowedOperation(roles, Operations.UPDATE_PRODUCT);
//        return !roles.contains(Roles.ADMIN) && !roles.contains(Roles.EDITOR) ; //&& este ala bun, daca contine unul din astea doi atunci e bine
//    }
//
//
//    private boolean userIsNotAllowedToDeleteProduct(Collection<Roles> roles) {
//        return userIsNotAllowedOperation(roles, Operations.UPDATE_PRODUCT);
//        return !roles.contains(Roles.ADMIN);
//
//    }

}
