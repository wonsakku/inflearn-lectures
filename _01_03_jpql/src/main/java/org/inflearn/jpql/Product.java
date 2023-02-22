package org.inflearn.jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;
    private int stocktAmount;

}
