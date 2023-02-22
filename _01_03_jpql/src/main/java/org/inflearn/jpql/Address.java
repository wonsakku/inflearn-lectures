package org.inflearn.jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Embeddable
@Setter
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

}
