package org.inflearn.repository;

import org.inflearn.entity.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberJpaRepository {

    @PersistenceContext
    private EntityManager em;



    public Member save(Member member){
        em.persist(member);
        return member;
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public void delete(Member member){
        em.remove(member);
    }

    public List<Member> findAll(){
        return em.createQuery("SELECT m FROM Member m", Member.class).getResultList();
    }

    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    public long count(){
        return em.createQuery("SELECT COUNT(m) FROM Member m", Long.class)
                .getSingleResult();
    }

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age){
        return em.createQuery("SELECT m FROM Member m " +
                        " WHERE username = :username " +
                        " AND age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }


    public List<Member> findByUsername(String username){
        return em.createNamedQuery("Member.findByUsername", Member.class)
                .setParameter("username", username)
                .getResultList();
    }


    public List<Member> findByPage(int age, int offset, int limit){
        return em.createQuery("SELECT m FROM Member m " +
                        " WHERE m.age = :age " +
                        " ORDER BY m.username DESC ")
                .setParameter("age", age)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public long totalCount(int age){
        return em.createQuery("SELECT COUNT(m) FROM Member m WHERE m.age = :age", Long.class)
                .setParameter("age", age)
                .getSingleResult();
    }

    public int bulkAgePlus(int age){
        int updateCount = em.createQuery("UPDATE Member m " +
                        " SET m.age = m.age +1 " +
                        " WHERE m.age >= :age")
                .setParameter("age", age)
                .executeUpdate();

        return updateCount;
    }

}





