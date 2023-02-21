package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class _14_JpaMain_EmbededType {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("kim0");
            member.setHomeAddress(new Address("경기도 성남시", "엄궁북로 62", "12345"));
//            member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now().plusYears(1l)));

            em.persist(member);


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



