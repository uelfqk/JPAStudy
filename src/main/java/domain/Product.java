package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    private int stockAmount;

    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();
}
