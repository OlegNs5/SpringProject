package ro.ctrln.entities;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.events.Event;
import ro.ctrln.enums.Roles;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Getter
@Setter
@Table(name = "shop_user")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "password")
    private String password;

    @Column(name = "surname")
    private String surname;

    @Column(name = "username")
    private String username;

        @Embedded
    private Address address;

        @ElementCollection
        @CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name = "user_id"))
        @Column(name = "roles")
        @Enumerated(EnumType.STRING)
        private Collection<Roles> roles;

}
