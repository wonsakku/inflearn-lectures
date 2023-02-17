package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity
public class OrderItem  extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

//    @Column(name = "order_id")
//    private Long orderId;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;


//    @Column(name = "item_id")
//    private Long itemId;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item itemId;

    private int orderPrice;
    private int count;

}
