
package hellojpa_02;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class _01_JpaMain {

    public static void  main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 transaction 안에서 일어나야 함.
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Movie movie = new Movie();
            movie.setDirector("aaa");
            movie.setActor("bbb");
            movie.setName("바람과 함께 사라지다");
            movie.setPrice(10000);

            em.persist(movie);

            Book book = new Book();
            book.setIsbn("aaa");
            book.setAuthor("bbb");
            book.setName("boooooooooooooook");
            book.setPrice(10000);

            em.persist(movie);
            em.persist(book);

            em.flush();
            em.clear();

            Movie findMovie = em.find(Movie.class, movie.getId());
            Item findItem = em.find(Item.class, book.getId());

//            System.out.println(findMovie);

            //code
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



