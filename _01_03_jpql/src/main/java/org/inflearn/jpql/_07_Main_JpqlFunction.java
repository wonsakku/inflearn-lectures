package org.inflearn.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _07_Main_JpqlFunction {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member");
            member.setAge(1);
            em.persist(member);

            em.flush();
            em.clear();

            // concat
//            String query = "SELECT 'a' || 'b' FROM Member m";
            String query = "SELECT CONCAT('a', 'b') FROM Member m";
            List<String> resultList = em.createQuery(query, String.class).getResultList();
            System.out.println(resultList);

            // substring
            query = "SELECT SUBSTRING(m.username, 2, 3) FROM Member m";
            resultList = em.createQuery(query, String.class).getResultList();
            System.out.println(resultList);

            // locate
            query = "SELECT locate('de', 'abcdefg') FROM Member m";
            em.createQuery(query, Integer.class).getResultList()
                    .forEach(System.out::println);

            // size
            query = "SELECT SIZE(t.members) FROM Team t";
            em.createQuery(query, Integer.class).getResultList()
                    .forEach(System.out::println);



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