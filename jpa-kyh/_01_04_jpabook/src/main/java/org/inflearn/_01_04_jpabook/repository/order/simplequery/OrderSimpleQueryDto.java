package org.inflearn._01_04_jpabook.repository.order.simplequery;

import lombok.Data;
import org.inflearn._01_04_jpabook.domain.Address;
import org.inflearn._01_04_jpabook.domain.Order;
import org.inflearn._01_04_jpabook.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
public class OrderSimpleQueryDto {


    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public OrderSimpleQueryDto(Long orderId,
                               String name,
                               LocalDateTime orderDate,
                               OrderStatus orderStatus,
                               Address address){

        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }

}
