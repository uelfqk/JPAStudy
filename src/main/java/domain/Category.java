package domain;

import domain.Items.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
public class Category extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "Category_Item",
               joinColumns = @JoinColumn(name = "Category_id"),
               inverseJoinColumns = @JoinColumn(name = "Item_Id")
    )
    private List<Item> items = new ArrayList<>();
}
