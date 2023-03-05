package org.inflearn.repository;

import org.inflearn.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    @Rollback(value = false)
    void testMember(){

        // given
        Member member = new Member("jpa");

        // when
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        assertThat(savedMember).isEqualTo(member);
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    @Rollback(value = false)
    void basicCrud(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("changed username");


        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long afterDeleteCount = memberJpaRepository.count();
        assertThat(afterDeleteCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan(){

        Member member1 = new Member("member", 10);
        Member member2 = new Member("member", 20);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> member = memberJpaRepository.findByUsernameAndAgeGreaterThan("member", 15);

        assertThat(member.get(0).getUsername()).isEqualTo("member");
        assertThat(member.get(0).getAge()).isEqualTo(20);
        assertThat(member.get(0)).isEqualTo(member2);
    }

    @Test
    void testNamedQuery(){
        Member member1 = new Member("member", 10);
        Member member2 = new Member("member2", 20);

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> findMembers = memberJpaRepository.findByUsername("member");

        for (Member m : findMembers) {
            System.out.println("member => " + m);
        }

        assertThat(findMembers.get(0)).isEqualTo(member1);
    }


    @Test
    void paging(){

        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 3;
        int limit = 3;

        // when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then
        assertThat(members).hasSize(2);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    @Rollback(value = false)
    void bulkUpdate(){
        // given
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));

        int updateCount = memberJpaRepository.bulkAgePlus(20);

        assertThat(updateCount).isEqualTo(3);
    }

}



