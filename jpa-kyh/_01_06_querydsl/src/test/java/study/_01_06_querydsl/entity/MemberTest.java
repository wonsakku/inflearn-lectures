package study._01_06_querydsl.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManager em;

    @Test
    void testEntity(){
        final Team teamA = new Team("teamA");
        final Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        final Member member1 = new Member("member1", 10, teamA);
        final Member member2 = new Member("member2", 20, teamA);
        final Member member3 = new Member("member3", 30, teamB);
        final Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        final List<Member> selectMFromMemberM = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();

        for (Member member : selectMFromMemberM) {
            System.out.println("member => " + member);
            System.out.println("-> member.team : " + member.getTeam());
        }
    }


}