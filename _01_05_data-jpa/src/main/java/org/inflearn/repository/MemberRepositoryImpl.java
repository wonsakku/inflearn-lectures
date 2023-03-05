package org.inflearn.repository;

import lombok.RequiredArgsConstructor;
import org.inflearn.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }
}
