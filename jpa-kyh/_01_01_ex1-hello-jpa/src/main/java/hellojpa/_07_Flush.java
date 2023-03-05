package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _07_Flush {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            Member member20 = new Member(20L, "member20");
            em.persist(member20);

            // 쿼리 실행
            em.flush();

            // flush는 영속성 컨텍스트를 비우지 않음
            // flush는 영속성 컨텍스트에 있는 쓰기 지연 SQL 저장소 안의 SQL들이 DB에 반영시킨다.(영속서 컨텍스트 변경 내용을 DB에 동기화)

            System.out.println("~~~~~~~~~~~~~~~~~~~~");

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
