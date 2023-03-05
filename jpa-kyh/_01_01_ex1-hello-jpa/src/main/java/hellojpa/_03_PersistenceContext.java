package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _03_PersistenceContext {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            // 비영속 상태
            Member member = new Member(6L, "HelloJPA3");

            // persist 후 entityManager 안의 영속성 컨텍스트에서 관리
            // 영속상태
            // persist에서 데이터가 DB에 저장되는 것이 아님.
            System.out.println("````````````BEFORE persist````````````");
            em.persist(member);

            // detach --> 영속성 콘텍스트에서 분리, 준영속 상태
//            em.detach(member);
            System.out.println("````````````AFTER persist````````````");

            // transaction commit 이 일어날 때 DB에 쿼리가 날라간다.
            System.out.println("``````````````````BEFORE COMMIT``````````````````");
            tx.commit();
            System.out.println("``````````````````AFTER COMMIT``````````````````");
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
