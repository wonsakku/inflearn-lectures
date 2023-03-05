package org.inflearn._01_04_jpabook.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.Address;
import org.inflearn._01_04_jpabook.domain.Order;
import org.inflearn._01_04_jpabook.domain.OrderItem;
import org.inflearn._01_04_jpabook.domain.OrderStatus;
import org.inflearn._01_04_jpabook.repository.OrderRepository;
import org.inflearn._01_04_jpabook.repository.OrderSearch;
import org.inflearn._01_04_jpabook.repository.order.query.OrderFlatDto;
import org.inflearn._01_04_jpabook.repository.order.query.OrderQueryDto;
import org.inflearn._01_04_jpabook.repository.order.query.OrderQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByCriteria(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            order.getOrderItems().forEach(OrderItem::getItem);
        }

        return all;
    }


    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByCriteria(new OrderSearch());

        List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3(){
        List<Order> orders =  orderRepository.findAllWithItem();
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }


    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(@RequestParam(name = "offset", defaultValue = "0") int offset,
                                       @RequestParam(name = "limit", defaultValue = "0") int limit){
        // xToOne 관계(oneToOne, manyToOne)는 fetch join으로 가져온다.
        List<Order> orders =  orderRepository.findAllWithMemberDelivery(offset, limit);

        // collection은 lazy loading으로 데이터를 가져온다.
        // jpa.properties.hibernate.default_batch_fetch_size 설정으로 collection의 값을 in query로 가져온다.
        return orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
    }



    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> orderV4(){
        return orderQueryRepository.findOrderQueryDtos();
    }


    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> orderV5(){
        return orderQueryRepository.findAllByDto_optimization();
    }



    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> orderV6(){
        return orderQueryRepository.findAllByDto_flat();
    }




    @Data
    static class OrderDto {

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order o){
            this.orderId = o.getId();
            this.name = o.getMember().getName();
            this.orderDate = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.address = o.getDelivery().getAddress();
            this.orderItems = o.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto{

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem){
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}





