package org.inflearn.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _10_Main_BulkOperation {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB =  new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Team teamC =  new Team();
            teamC.setName("팀C");
            em.persist(teamC);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            Member member4 = new Member();
            member4.setUsername("회원4");
            em.persist(member4);

//            em.flush();
//            em.clear();

            // 쿼리를 직접 실행하면 영속성 컨텍스트가 flush됨. clear는 되지 않음.
            int resultCount = em.createQuery("UPDATE Member m SET m.age = 20")
                    .executeUpdate();

            System.out.println("resultCount : " + resultCount);

            // 직접 쿼리를 날리는 경우에는 그 결과가 영속성 컨텍스트에 반영되지 않음.
            // 벌크 연산 후 영속성 컨텍스트를 clear해줘야 함.
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println(em.find(Member.class, member1.getId()));
            System.out.println(em.find(Member.class, member2.getId()));
            System.out.println(em.find(Member.class, member3.getId()));
            System.out.println(em.find(Member.class, member4.getId()));

            em.clear();

            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println(em.find(Member.class, member1.getId()));
            System.out.println(em.find(Member.class, member2.getId()));
            System.out.println(em.find(Member.class, member3.getId()));
            System.out.println(em.find(Member.class, member4.getId()));

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