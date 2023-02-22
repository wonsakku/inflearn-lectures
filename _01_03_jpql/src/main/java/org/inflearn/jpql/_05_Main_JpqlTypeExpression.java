package org.inflearn.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _05_Main_JpqlTypeExpression {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {


            Member member = new Member();
            member.setUsername("member");
            member.setAge(1);
            member.setType(MemberType.ADMIN);
            em.persist(member);

            em.flush();
            em.clear();

            String query = "SELECT m.username, 'HELLO', true, type FROM Member m " +
                    " WHERE m.type = org.inflearn.jpql.MemberType.ADMIN ";
            List<Object[]> resultList = em.createQuery(query).getResultList();

            for (Object[] objects : resultList) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
                System.out.println("objects = " + objects[3]);
            }


            query = "SELECT m.username, 'HELLO', true, type FROM Member m " +
                    " WHERE m.type = :userType ";
            List<Object[]> resultList2 = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();

            for (Object[] objects : resultList2) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
                System.out.println("objects = " + objects[3]);
            }




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