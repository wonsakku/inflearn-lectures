package org.inflearn.jpql;

import javax.persistence.*;
import java.util.List;

public class _03_Main_Pagination {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            for (int i = 1 ; i < 101 ; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result = em.createQuery("SELECT m FROM Member m order by m.age DESC", Member.class)
                    .setFirstResult(20)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("size : " + result.size());
            result.forEach(System.out::println);

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