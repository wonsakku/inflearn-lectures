package org.inflearn._01_04_jpabook.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager em;


    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> findOrders = findOrders();

        findOrders.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return findOrders;
    }

    private List<OrderQueryDto> findOrders() {
        List<OrderQueryDto> findOrders = em.createQuery(
                     "SELECT new org.inflearn._01_04_jpabook.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        " FROM Order o " +
                        " JOIN o.member m " +
                        " JOIN o.delivery d", OrderQueryDto.class)
                .getResultList();
        return findOrders;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "SELECT new org.inflearn._01_04_jpabook.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        " FROM OrderItem oi " +
                        " JOIN oi.item i " +
                        " WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = toOrderId(result);

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "SELECT new org.inflearn._01_04_jpabook.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                " FROM OrderItem oi " +
                                " JOIN oi.item i " +
                                " WHERE oi.order.id IN :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
        return orderItemMap;
    }

    private static List<Long> toOrderId(List<OrderQueryDto> result) {
        List<Long> orderIds = result.stream().map(fo -> fo.getOrderId()).collect(toList());
        return orderIds;
    }

    public List<OrderQueryDto> findAllByDto_flat() {
        List<OrderFlatDto> flats = em.createQuery(
                        "SELECT new org.inflearn._01_04_jpabook.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                                " FROM Order o " +
                                " JOIN o.member m " +
                                " JOIN o.delivery d " +
                                " JOIN o.orderItems oi " +
                                " JOIN oi.item i", OrderFlatDto.class)
                .getResultList();

        // api 스펙에 맞게 데이터 변경
        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                         mapping(o -> new OrderItemQueryDto(o.getOrderId(),o.getItemName(), o.getOrderPrice(), o.getCount()), toList()))
                ).entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(),
                        e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),
                        e.getKey().getAddress(), e.getValue()))
                .collect(toList());
    }
}













