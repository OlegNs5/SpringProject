package ro.ctrln.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ro.ctrln.dtos.OrderDTO;
import ro.ctrln.exceptions.*;
import ro.ctrln.services.OrderService;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{customerId}")
    public void addOrder(@RequestBody OrderDTO orderDTO, @PathVariable Long customerId) throws InvalidProductIdException,
            InvalidQuantityException, NotEnoughStockException, InvalidProductsException, InvalidCustomerIdException {
        orderService.addOrder(orderDTO,customerId);
    }

    @PatchMapping("/{orderId}/{customerId}")
    public void deliverOrder(@PathVariable Long orderId,@PathVariable Long customerId) throws InvalidOrderIdException,
            OrderCanceledException, OrderDeliveredException {
        orderService.deliverOrder(orderId,customerId);
    }

    @PatchMapping("/cancel/{orderId}/{customerId}")
    public void cancelOrder (@PathVariable Long orderId,@PathVariable Long customerId) throws OrderCanceledException,
            InvalidOrderIdException, OrderDeliveredException {
        orderService.cancelOrder(orderId,customerId);
    }

    @PatchMapping("/return/{orderId}/{customerId}")
    public void returnlOrder (@PathVariable Long orderId,@PathVariable Long customerId) throws OrderCanceledException,
            InvalidOrderIdException, OrderDeliveredException, OrderNotYetDeliveredException {
        orderService.returnOrder(orderId,customerId);
    }
}
