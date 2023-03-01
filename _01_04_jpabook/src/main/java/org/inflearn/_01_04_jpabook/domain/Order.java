package org.inflearn._01_04_jpabook.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

//    @BatchSize(size = 1000)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문 시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문 상태 [ORDER, CANCEL]

    // ========== 연관관계 메서드 ==========
    public void setMember(Member member){
        this.member = member;
        this.member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        this.delivery.setOrder(this);
    }

    // == 생성 메서드 == //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){

        Order order = new Order();

        order.member = member;
        order.delivery = delivery;
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.status = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();

        return order;
    }

    // == 비즈니스 로직 == //
    /**
     * 주문 취소
     */
    public void cancel(){
        if(this.delivery.getDeliveryStatus() == DeliveryStatus.COMP){
            throw new IllegalArgumentException("이미 배송완료된 사움은 취소가 불가능합니다.");
        }
        this.status = OrderStatus.CANCEL;

        this.orderItems.forEach(OrderItem::cancel);
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        return this.orderItems.stream()
                .mapToInt(OrderItem::getTotalPrice)
                .sum();
    }


}
