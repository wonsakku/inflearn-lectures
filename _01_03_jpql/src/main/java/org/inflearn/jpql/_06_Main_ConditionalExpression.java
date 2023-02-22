package org.inflearn.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _06_Main_ConditionalExpression {

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

            em.persist(new Member());

            em.flush();
            em.clear();

            // case when
            String query = "SELECT " +
                    " CASE WHEN m.age <= 10 THEN '학생요금' " +
                    "      WHEN m.age >= 60 THEN '경로요금' " +
                    "      ELSE '일반요금' " +
                    " END AS price " +
                    " FROM Member m ";

            List<String> results = em.createQuery(query, String.class).getResultList();
            results.forEach(System.out::println);

            // coalesce
            query = "SELECT COALESCE(m.username, '이름없는 회원') FROM Member m";

            results = em.createQuery(query, String.class).getResultList();
            results.forEach(System.out::println);

            // nullIf
            query = "SELECT NULLIF(COALESCE(m.username, '이름없는 회원'), 'member100') FROM Member m";

            results = em.createQuery(query, String.class).getResultList();
            results.forEach(System.out::println);


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