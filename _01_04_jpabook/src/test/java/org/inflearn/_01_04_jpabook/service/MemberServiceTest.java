package org.inflearn._01_04_jpabook.service;

import org.assertj.core.api.Assertions;
import org.inflearn._01_04_jpabook.domain.Member;
import org.inflearn._01_04_jpabook.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(value = false)
    public void 회원가입() throws Exception{

        // given
        Member member = new Member();
        member.setName("Kim");

        // when
        Long joinId = memberService.join(member);

        // then
        em.flush();
        assertThat(member).isEqualTo(memberRepository.findOne(joinId));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 중복_회원_예외() throws Exception{
        // given
        Member member1 = new Member();
        member1.setName("Kim");

        Member member2 = new Member();
        member2.setName("Kim");

        // when
        memberService.join(member1);
        memberService.join(member2);

        // then
        Assert.fail("예외가 발생해야 한다");
//        assertThatThrownBy(() -> memberService.join(member2))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("이미 존재하는 회원입니다.");



    }

}