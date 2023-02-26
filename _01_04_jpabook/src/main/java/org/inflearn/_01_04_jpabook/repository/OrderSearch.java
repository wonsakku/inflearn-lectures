package org.inflearn._01_04_jpabook.repository;

import lombok.Getter;
import lombok.Setter;
import org.inflearn._01_04_jpabook.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {
    private String memberName; // 회원 이름
    private OrderStatus orderStatus;  // 주문상태 [ORDER, CANCEL]
}
