package ro.ctrln.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class OnlineOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = ("user_id"))
    private User user;

    @OneToMany(cascade = CascadeType.ALL) //De obicei nu trebuie sa stergem din baza de date "delete from *" il rogi sa nu il mai afiseze
    @JoinColumn(name = "order_id")
    private List<OnlineOrderItem> orderItems;

    private double totalPrice;

    private boolean Delivered;
    private boolean Returned;
    private boolean Cancelled;

}
