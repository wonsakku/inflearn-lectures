package org.inflearn.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _04_Main_Join {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("team1");

            em.persist(team);

            Member member = new Member();
            member.setUsername("team1");
            member.setAge(1);
            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            String joinQuery = "SELECT m FROM Member m INNER JOIN m.team t WHERE t.name = :name ORDER BY m.age DESC";

            List<Member> joinQueryResult = em.createQuery(joinQuery, Member.class)
                    .setParameter("name", "team1")
                    .getResultList();


            String thetaJoin = "SELECT m FROM Member m, Team t WHERE m.username = t.name";
            List<Member> thetaJoinResults = em.createQuery(thetaJoin, Member.class).getResultList();

            System.out.println(thetaJoinResults.size());
            System.out.println(thetaJoinResults);

            em.createQuery("SELECT m FROM Member m LEFT JOIN m.team t ON t.name = 'team1'");
            em.createQuery("SELECT m FROM Member m LEFT JOIN Team t ON m.username = t.name");


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