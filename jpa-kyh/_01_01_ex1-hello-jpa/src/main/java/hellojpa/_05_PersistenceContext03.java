package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _05_PersistenceContext03 {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // Transaction을 지원하는 쓰기 지연
        try{
            Member member = new Member(10L, "HelloJPA10");
            Member member2 = new Member(11L, "HelloJPA11");
            em.persist(member);
            em.persist(member2);
            // 여기까지 INSERT SQL을 DB에 보내지 않는다.
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


            // persistence.xml에서 hibernate.jdbc.batch_size 설정을 주면
            // batch_size만큼 배치처리 가능
            // 커밋하는 순간 DB에 INSERT SQL을 보낸다.
            tx.commit(); // Transaction Commit
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
