package org.inflearn.jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    private int orderAmount;

    @Embedded
    private Address address;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
