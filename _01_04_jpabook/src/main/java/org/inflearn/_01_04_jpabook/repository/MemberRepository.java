package org.inflearn._01_04_jpabook.repository;

import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class MemberRepository {

//    @Autowired
//    @PersistenceContext
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name){
        return em.createQuery("SELECT m FROM Member m WHERE name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }


}











