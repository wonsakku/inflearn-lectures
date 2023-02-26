package org.inflearn._01_04_jpabook.domain;

import lombok.Getter;
import lombok.Setter;
import org.inflearn._01_04_jpabook.domain.item.Item;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; // 주문 수량
}
