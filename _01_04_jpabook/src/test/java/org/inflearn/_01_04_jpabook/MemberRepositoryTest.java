package org.inflearn._01_04_jpabook;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional // entityManager는 트랜잭션 안에서 실행되어야 함.
    @Rollback(false)
    public void testMember() throws Exception {

        Member member = new Member();
        member.setUsername("testUser");

        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);


        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        // transaction이 끝나지 않았기 때문에 entityManager가 clear되지 않음.
        // 그래서 findMember와 member는 같은 객체
        assertThat(findMember).isEqualTo(member);
    }
}