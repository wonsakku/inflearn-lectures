package hellojpa;

import lombok.*;

import javax.persistence.Embeddable;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;

}
