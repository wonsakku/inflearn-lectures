package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _09_PrimaryKeyMapping {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Member member = new Member();
            member.setUsername("C1232C");

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            // 기본키 전략이 자동생성(?)일 경우 persist 시점에서 쿼리가 실행된다.
            // 영속성 컨텍스트는 (key, Entity)로 1차 캐시를 관리하는데
            // 기본키 생성 전략이 자동일 경우 DB에 insert시켜봐야 그 값을 알 수 있기 때문에
            // persist 실행 시 쿼리를 날리게 된다.
            // sequence를 조회하는 경우 sequence의 다음 값을 가져오는 쿼리가 실행된다.
            // 요는 영속성 컨텍스트에 (key, Entity) 로 Entity가 관리된다는 점.
            em.persist(member);
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
