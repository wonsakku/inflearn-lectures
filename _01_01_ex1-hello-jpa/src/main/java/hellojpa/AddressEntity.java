package hellojpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id @GeneratedValue
    private Long id;

    private Address address;

    public AddressEntity(Address address) {
        this.address = address;
    }
}
