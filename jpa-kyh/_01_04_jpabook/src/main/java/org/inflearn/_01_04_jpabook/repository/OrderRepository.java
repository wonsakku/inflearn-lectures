package org.inflearn._01_04_jpabook.repository;

import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.Member;
import org.inflearn._01_04_jpabook.domain.Order;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        String jpql = "SELECT o FROM Order o join o.member m WHERE m.name LIKE :name AND o.orderStatus = :orderStatus";

        boolean isFirstCondition = true;

        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " WHERE ";
                isFirstCondition = false;
            } else {
                jpql += " AND ";
            }
            jpql += " o.status = :status ";
        }

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " WHERE ";
                isFirstCondition = false;
            } else {
                jpql += " AND ";
            }
            jpql += " m.name LIKE :name ";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class);

        if (orderSearch.getOrderStatus() != null) {
            query.setParameter("status", orderSearch.getOrderStatus());
        }

        if (orderSearch.getMemberName() != null) {
            query.setParameter("name", orderSearch.getMemberName());
        }

        return query.getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
            criteria.add(name);

        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대
        return query.getResultList();
    }


    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery("SELECT o FROM Order o " +
                        " JOIN FETCH o.member " +
                        " JOIN FETCH o.delivery", Order.class)
                .getResultList();
    }



    public List<Order> findAllWithItem() {
        return em.createQuery("SELECT DISTINCT o FROM Order o " +
                " JOIN FETCH o.member " +
                " JOIN FETCH o.delivery d " +
                " JOIN FETCH o.orderItems oi " +
                " JOIN FETCH oi.item i ", Order.class)
                .getResultList();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return em.createQuery("SELECT o FROM Order o " +
                        " JOIN FETCH o.member " +
                        " JOIN FETCH o.delivery", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
}
