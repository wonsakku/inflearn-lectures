package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _10_JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //code
            Team team = new Team();
            team.setName("TeamA");

            // persist로 영속상태가 되면 id가 부여됨.
            // id가 key이기 때문에 key의 값이 없다면 db에서 key값을 조회 또는 데이터 insert하여 객체에 key값이 setting 됨.
            em.persist(team);

            // Member 객체 설계시 id를 자동생성으로 했기 때문에 id값을 setting해서 persist하면 오류가 발생함.
            Member member = new Member();
            member.setUsername("member100");
            member.setTeam(team);

            em.persist(member);

            // select 쿼리문이 날라가는 것을 보기 위해 영속성 context 를 clear
            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();

            System.out.println("findTeam : " + findTeam);
//            System.out.println("team : " + team);
//            System.out.println("team = findTeam : " + (team == findTeam));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            // DB connection 반환
            em.close();
        }

        emf.close();

    }
}



