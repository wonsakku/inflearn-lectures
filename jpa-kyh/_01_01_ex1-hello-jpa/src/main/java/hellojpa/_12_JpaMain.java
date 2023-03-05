
package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class _12_JpaMain {

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
//            member.setTeam(team);

            // 객체의 관점에서 접근

            // flush, clear 하지 않으면 데이터 조회 시 영속성 context의 1차 캐시에서 데이터를 조회하기 때문에
            // 그래서 team에도 members.add를 하지 않으면 team의 members size가 0으로 나온다.
            // 그렇기 때문에 양방향 연관관계에서 양쪽에 값을 셋팅하는게 좋다??
//            team.getMembers().add(member);

            // 연관관계 편의 메서드를 생성하자.
            member.changeTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();
            List<Member> members = findTeam.getMembers();

            // 양방향 관계일 때 toString, lombok, JSON 라이브러리 등에 의한 무한루프 주의
            members.forEach(System.out::println);


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



