package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class _15_JpaMain_ValueTypeAndImmutableObject {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Address address = new Address("경기도 성남시", "엄궁북로 62", "12345");

            Member member = new Member();
            member.setUsername("kim0");
            member.setHomeAddress(address);

            Member member2 = new Member();
            member2.setUsername("kim1");
            member2.setHomeAddress(address);
            member2.setHomeAddress(new Address(address.getCity(), address.getStreet(), address.getZipcode()));

            em.persist(member);
            em.persist(member2);

            member2.getHomeAddress().setCity("uuuuuuuuuu");


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



