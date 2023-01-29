package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _01_JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            //code
            //등록
//            Member sakku = new Member(3L, "hello-jpa");
//            em.persist(sakku);

            // 조회
            Member wonsakku = em.find(Member.class, 1L);

            System.out.println("findMember.id = " + wonsakku.getId());
            System.out.println("findMember.name = " + wonsakku.getName());

            // 수정 --> em.persist를 안해도 됨.
            // 기본 생성자 있어야 함??
            // 없으면 나는 에러 : [ org.hibernate.InstantiationException: No default constructor for entity:  : hellojpa.Member ]
            wonsakku.setName("wonsakku");

            // 삭제
//            em.remove(wonsakku);

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



