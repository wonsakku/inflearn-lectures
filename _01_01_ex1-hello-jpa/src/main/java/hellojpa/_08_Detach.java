package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _08_Detach {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            // 준영속 상태 -> 영속성 컨텍스트에서 관리하는 객체를 영속성 컨텍스트가 관리하지 않게 되는 상태?
            Member member = em.find(Member.class, 20L);
            member.setName("AAAAAAAAAAA");

            // 3가지 방법으로 Entity를 준영속 상태로 변경 가능
            em.detach(member);
//            em.clear();
//            em.close();

            // update가 일어나지 않음
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }
}
