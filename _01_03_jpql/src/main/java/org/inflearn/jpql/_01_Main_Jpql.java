package org.inflearn.jpql;

import javax.persistence.*;

public class _01_Main_Jpql {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("SELECT m FROM Member m WHERE m.id = 10", Member.class);
            TypedQuery<Member> query2 = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class);
            query2.setParameter("username", "member1");
            Member singleResult = query2.getSingleResult();
            System.out.println("singleResult : " + singleResult.getUsername());

            Member singleResult1 = em.createQuery("SELECT m FROM Member m WHERE m.username = :username", Member.class)
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("singleResult1 : " + singleResult1.getUsername());


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