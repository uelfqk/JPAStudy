package domain;

import javax.persistence.*;

@Entity
@Table(name = "t_address")
public class AddressEntity {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Long id;

    Address address;

    public AddressEntity(String city, String street, String zipcode) {
        this.address = new Address(city, street, zipcode);
    }

    protected AddressEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
