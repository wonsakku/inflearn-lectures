package org.inflearn._01_04_jpabook.service;

import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.Delivery;
import org.inflearn._01_04_jpabook.domain.Member;
import org.inflearn._01_04_jpabook.domain.Order;
import org.inflearn._01_04_jpabook.domain.OrderItem;
import org.inflearn._01_04_jpabook.domain.item.Item;
import org.inflearn._01_04_jpabook.repository.ItemRepository;
import org.inflearn._01_04_jpabook.repository.MemberRepository;
import org.inflearn._01_04_jpabook.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장 ==> Cascade.ALL 때문에 order만 저장해도 배송정보, 주문상품 모두 저장됨.
        orderRepository.save(order);

        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }


    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }

}

