package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _06_PersistenceContext04 {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 변경 감지 (dirty check)
        try{
            Member findMember = em.find(Member.class, 10L);

            // findMember 시 영속성 컨텍스트의 1차 캐시에 캐싱하기 때문에
            // findMember는 영속 상태에 있음.
            // 즉, em.persist를 하지 않아도 findMember는 영속 상태이기 때문에
            // 데이터가 변경이 일어나면 변경을 감지해서 tx.commit시 업데이트를 함.
            findMember.setUsername("ZZZZZZ");

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
