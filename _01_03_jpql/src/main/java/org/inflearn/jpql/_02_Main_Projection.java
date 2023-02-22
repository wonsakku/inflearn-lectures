package org.inflearn.jpql;

import javax.persistence.*;
import java.util.List;

public class _02_Main_Projection {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            List<Member> result = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
            result.get(0).setAge(200);
            List<Team> joinResult = em.createQuery("SELECT m.team FROM Member m join m.team t", Team.class).getResultList();

            List<Address> valueTypeResult = em.createQuery("SELECT o.address FROM Order o", Address.class).getResultList();

            List scalaType = em.createQuery("SELECT distinct m.username, m.age FROM Member m").getResultList();

            List scalaTypeDto = em.createQuery("SELECT new org.inflearn.jpql.MemberDto(m.username, m.age) FROM Member m", MemberDto.class).getResultList();

            scalaTypeDto.forEach(System.out::println);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            // DB connection 반환
            em.close();
        }

        emf.close();
    }
}