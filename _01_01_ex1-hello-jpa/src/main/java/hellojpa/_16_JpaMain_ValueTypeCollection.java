package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class _16_JpaMain_ValueTypeCollection {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setHomeAddress(new Address("city1", "street1", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new AddressEntity(new Address("old2", "street2", "100002")));
            member.getAddressHistory().add(new AddressEntity(new Address("old3", "street3", "100003")));

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");
            Member findMember = em.find(Member.class, member.getId());

//            System.out.println("````````````지연로딩````````````");
//            List<Address> addressHistory = findMember.getAddressHistory();
//            addressHistory.forEach(addr -> System.out.println(addr.getCity()));
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            favoriteFoods.forEach(System.out::println);

            System.out.println("``````````````````````update``````````````````````");
            Address homeAddress = findMember.getHomeAddress();
            findMember.setHomeAddress(new Address("newCity",homeAddress.getStreet(), homeAddress.getZipcode()));

            // 치킨 -> 한식
            findMember.getFavoriteFoods().remove("치킨");
            findMember.getFavoriteFoods().add("한식");

            // equals로 컬렉션을 제거함
//            findMember.getAddressHistory().remove(new Address("old2", "street2", "100002"));
//            findMember.getAddressHistory().add(new Address("newCity2", "street2", "100002"));
//            member.getAddressHistory().add(new AddressEntity(new Address("old2", "street2", "100002")));
//            member.getAddressHistory().add(new AddressEntity(new Address("old3", "street3", "100003")));




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



