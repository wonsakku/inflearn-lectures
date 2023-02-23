package org.inflearn.jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _08_Main_FetchJoin {

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

            em.flush();
            em.clear();


            // N + 1 문제는 join fetch로 해결
//            String query = "SELECT m FROM Member m";
//            String query = "SELECT m FROM Member m LEFT JOIN FETCH m.team";
            String query = "SELECT m FROM Member m JOIN FETCH m.team";

            List<Member> results = em.createQuery(query, Member.class).getResultList();

            for (Member result : results) {
                if(result.getTeam() != null){
                    System.out.println(result);
                    System.out.println(result.getTeam().getName());
                }else{
                    System.out.println(result);
                }
            }


            //////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////

            em.clear();

            // 1:N join일 경우 데이터 뻥튀기
//            query = "SELECT t FROM Team t LEFT JOIN FETCH t.members m";
//            query = "SELECT t FROM Team t JOIN FETCH t.members m";
            query = "SELECT DISTINCT t FROM Team t JOIN FETCH t.members";

            List<Team> teamResults = em.createQuery(query, Team.class).getResultList();

            for(Team team : teamResults){
                System.out.println("team = " + team.getName() + ", " + team.getMembers().size());

                for(Member member : team.getMembers()){
                    System.out.println(" - member = " + member);
                }
            }

            //////////////////////////////////////////////////////////////////////////////
            //////////////////////////////////////////////////////////////////////////////

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~`");
            em.clear();

            query = "SELECT t FROM Team t";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();

            for(Team team : resultList){
                System.out.println("team = " + team.getName() + ", " + team.getMembers().size());

                for(Member member : team.getMembers()){
                    System.out.println(" - member = " + member);
                }
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