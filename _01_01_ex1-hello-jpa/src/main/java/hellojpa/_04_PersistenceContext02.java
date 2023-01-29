package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _04_PersistenceContext02 {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            // 비영속 상태
            Member member = new Member(8L, "HelloJPA5");
            em.persist(member);

            // em.find 시 영속성 컨텍스트에 캐싱되어 있으면 1차 캐시에 캐싱되어 있는 데이터를 보여줌
            // 없으면 db 에서 select

            // select 쿼리가 실행되지 않음
            Member findMember2 = em.find(Member.class, 8L);
            Member findMember22 = em.find(Member.class, 8L);
            System.out.println("member : " + findMember22);

            System.out.println();
            System.out.println();

            // select 쿼리가 실행됨
            Member findMember1 = em.find(Member.class, 7L);
            Member findMember11 = em.find(Member.class, 7L);
            System.out.println("member : " + findMember1);


            // 영속 엔티티의 동일성 보장
            System.out.println(findMember22 == findMember2);
            System.out.println(findMember11 == findMember1 );

//            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
