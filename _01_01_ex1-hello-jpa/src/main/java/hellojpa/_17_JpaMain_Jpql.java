package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class _17_JpaMain_Jpql {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // JPQL
            String query = "SELECT m FROM Member m WHERE username LIKE '%kim%'";
            List<Member> members = em.createQuery(query, Member.class).getResultList();
            members.forEach(member -> System.out.println(member.getUsername()));

            // Criteria
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> criteriaQuery = cb.createQuery(Member.class);

            Root<Member> m = criteriaQuery.from(Member.class);

            CriteriaQuery<Member> cq = criteriaQuery.select(m).where(cb.equal(m.get("username"), "kim"));
            List<Member> resultList = em.createQuery(cq).getResultList();

            resultList.forEach(result -> System.out.println(result.getUsername()));

            // native query
            List resultList1 = em.createNativeQuery("SELECT * FROM member").getResultList();


            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            // DB connection 반환
            em.close();
        }

        emf.close();

    }
}



