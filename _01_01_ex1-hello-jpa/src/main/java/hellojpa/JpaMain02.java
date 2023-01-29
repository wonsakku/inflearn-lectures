package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain02 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // JPQL 조회 with WHERE
            Member findMember = em.find(Member.class, 1L);
            List<Member> result = em.createQuery("SELECT m FROM Member AS m WHERE m.name LIKE '%' || 'sakku' || '%'", Member.class)
                    // pagination
                    .setFirstResult(0)
                    .setMaxResults(5)
                    // end of pagination
                    .getResultList();


            result.forEach(System.out::println);

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
