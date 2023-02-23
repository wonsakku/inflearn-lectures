package org.inflearn._01_04_jpabook;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 커멘드와 쿼리를 분리하라 -->
    // insert는 커멘드성 명령이기 때문에 리턴값을 안만드는 것이 좋다.
    // but 리턴값이 id정도\면 그 값으로 다시 조회할 수 있기 때문에 id만 리턴?
    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
