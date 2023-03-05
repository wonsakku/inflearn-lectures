package org.inflearn.repository;

import org.inflearn.dto.MemberDto;
import org.inflearn.entity.Member;
import org.inflearn.entity.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    void checkMemberRepository(){
        // memberRepository class ==> class com.sun.proxy.$Proxy107
        System.out.println("memberRepository class ==> " + memberRepository.getClass());
    }

    @Test
    void testMember(){

        // given
        Member member = new Member("jpa");

        // when
        Member savedMember = memberRepository.save(member);
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        // then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
        assertThat(savedMember).isEqualTo(member);
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void basicCrud(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        // 단건 조회 검증
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("changed username");


        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long afterDeleteCount = memberRepository.count();
        assertThat(afterDeleteCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThan(){

        Member member1 = new Member("member", 10);
        Member member2 = new Member("member", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> member = memberRepository.findByUsernameAndAgeGreaterThan("member", 15);

        assertThat(member.get(0).getUsername()).isEqualTo("member");
        assertThat(member.get(0).getAge()).isEqualTo(20);
        assertThat(member.get(0)).isEqualTo(member2);
    }

    @Test
    void findHelloBy(){

        Member member1 = new Member("member1", 10);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 30);
        Member member4 = new Member("member4", 40);
        Member member5 = new Member("member5", 50);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);

        List<Member> hellBy = memberRepository.findTop3HelloBy();


        for (Member member : hellBy) {
            System.out.println("member -> " + member);
        }
    }


    @Test
    void testNamedQuery(){
        Member member1 = new Member("member", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findByUsername("member");

        for (Member member : findMembers) {
            System.out.println("member => " + member);
        }

        assertThat(findMembers.get(0)).isEqualTo(member1);
    }



    @Test
    void testQuery(){
        Member member1 = new Member("member", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findMembers = memberRepository.findUser("member", 10);

        for (Member member : findMembers) {
            System.out.println("member => " + member);
        }

        assertThat(findMembers.get(0)).isEqualTo(member1);
    }


    @Test
    void findUsernameList(){
        Member member1 = new Member("member", 10);
        Member member2 = new Member("member2", 20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> names = memberRepository.findUsernameList();

        for (String name : names) {
            System.out.println(name);
        }
    }

    @Test
    void findMemberDto(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);


        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        memberRepository.findMemberDto().forEach(System.out::println);
    }



    @Test
    void findByNames(){
        Member member1 = new Member("member", 10);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 30);
        Member member4 = new Member("member4", 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

//        List<Member> findMembers = memberRepository.findByNames(Arrays.asList("member2","member4"));
        List<Member> findMembers = memberRepository.findByNames(List.of("member2","member4"));

        for (Member member : findMembers) {
            System.out.println("member -> " + member);
        }

        assertThat(findMembers.get(0)).isEqualTo(member2);
        assertThat(findMembers.get(1)).isEqualTo(member4);
    }



    @Test
    void returnType(){
        Member member1 = new Member("member", 10);
        Member member2 = new Member("member2", 20);
        Member member3 = new Member("member3", 30);
        Member member4 = new Member("member4", 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        em.flush();
        em.clear();


        System.out.println("~~~~~~~~~~~~~~~");

        List<Member> memberList = memberRepository.findListByUsername("member2");
        Member findMember = memberRepository.findMemberByUsername("member");
        Member findMemberA = memberRepository.findMemberByUsername("member");
        Member findMemberB = memberRepository.findMemberByUsername("member");
        Member findMemberC = memberRepository.findMemberByUsername("member");

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        memberRepository.findById(findMember.getId());
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        Optional<Member> optional = memberRepository.findOptionalByUsername("member");
        Optional<Member> findOptional = memberRepository.findOptionalByUsername("member3");

        List<Member> result = memberRepository.findListByUsername("member2asdfasdf");

        assertThat(result).hasSize(0);

    }



    @Test
    void paging(){

        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")   );

        // when
        Page<Member> page = memberRepository.findByAge(age, pageRequest);
        Page<MemberDto> memberDto = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));


        // then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        assertThat(content).hasSize(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }




    @Test
    void slice(){

        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")   );

        // when
        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        // then
        List<Member> content = page.getContent();

        assertThat(content).hasSize(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }


    @Test
    void pagingList(){

        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")   );

        // when
        List<Member> page = memberRepository.findMemberPagingByAge(age, pageRequest);
    }


    @Test
    void countQuery(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        // given
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 10, teamA));
        memberRepository.save(new Member("member3", 10, teamB));
        memberRepository.save(new Member("member4", 10, teamB));
        memberRepository.save(new Member("member5", 10, teamB));

        em.flush();
        em.clear();

        int age = 10;
        Pageable pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username")   );

        // when
        Page<Member> page = memberRepository.findCountQueryByAge(age, pageRequest);


        for (Member member : page) {
            System.out.println(member.getTeam().getName());
        }
    }

    @Test
    void bulkUpdate(){
        // given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // bulk 연산 결과는 영속성 context 반영되지 않음 --> bulk 연산 후 em.clear (flush는 jpql 쿼리 실행 시 flush 하기 때문에 flush는 필요업지 않음?)
        // @Modifying 에 clearAutomatically = true 옵션 추가하면 자동으로 clear 해줌
        int updateCount = memberRepository.bulkAgePlus(20);

//        Member member5 = memberRepository.findMemberByUsername("member5");
//        assertThat(member5.getAge()).isEqualTo(40);
//
//        assertThat(updateCount).isEqualTo(3);

//        em.flush();
//        em.clear();

        Member member5 = memberRepository.findMemberByUsername("member5");
        assertThat(member5.getAge()).isEqualTo(41);

        assertThat(updateCount).isEqualTo(3);
    }

    @Test
    void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member => " + member.getUsername());
            System.out.println("member.teamClass => " + member.getTeam().getClass());
            System.out.println("member.team.id => " + member.getTeam().getId());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("member.team.name => " + member.getTeam().getName());
        }
    }


    @Test
    void findMemberFetchJoin(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findMemberFetchJoin();

        for (Member member : members) {
            System.out.println("member => " + member.getUsername());
            System.out.println("member.teamClass => " + member.getTeam().getClass());
            System.out.println("member.team.id => " + member.getTeam().getId());
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            System.out.println("member.team.name => " + member.getTeam().getName());
        }

        System.out.println("##############################");

        Member member_01 = memberRepository.findById(members.get(0).getId()).get();
        Member member_02 = memberRepository.findById(members.get(1).getId()).get();

        System.out.println(member_01);
        System.out.println(member_01.getTeam().getName());
        System.out.println(member_02);
        System.out.println(member_02.getTeam().getName());
    }


    @Test
    void queryHint(){

        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);

        em.flush();
        em.clear();

        // when
        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }


    @Test
    void lock(){

        // given
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);

        em.flush();
        em.clear();

        // when
        List<Member >findMember = memberRepository.findLockByUsername(member1.getUsername());

    }


    @Test
    void callCustom(){
        List<Member> memberCustom = memberRepository.findMemberCustom();
    }

    @Test
    void projections(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnly> result = memberRepository.findProjectionsByUsername("m1");

        for (UsernameOnly usernameOnly : result) {
            System.out.println("usernameOnly = " + usernameOnly);
        }
    }


    @Test
    void projectionsDto(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnlyDto> result = memberRepository.findProjectionDtoByUsername("m1");

        for (UsernameOnlyDto usernameOnlyDto : result) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto);
            System.out.println("usernameOnlyDto.username = " + usernameOnlyDto.getUsername());
            System.out.println("usernameOnlyDto.age = " + usernameOnlyDto.getAge());
        }
    }


    @Test
    void projectionsGenericDto(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        List<UsernameOnlyDto> result = memberRepository.findProjectionGenericDtoByUsername("m1", UsernameOnlyDto.class);

        for (UsernameOnlyDto usernameOnlyDto : result) {
            System.out.println("usernameOnlyDto = " + usernameOnlyDto);
            System.out.println("usernameOnlyDto.username = " + usernameOnlyDto.getUsername());
            System.out.println("usernameOnlyDto.age = " + usernameOnlyDto.getAge());
        }
    }


    @Test
    void nativeQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();


        System.out.println("##############################################");
        Member findMember = memberRepository.findByNativeQuery("m1");
        System.out.println("native query ==> " + findMember);

        System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        Member byUsername = memberRepository.findMemberByUsername("m1");
        System.out.println("by username ==> " + byUsername);

        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        Optional<Member> byId = memberRepository.findById(m1.getId());
        System.out.println("by id ==> " + byId);

    }


    @Test
    void nativeProjectionQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1 = new Member("m1", 0, teamA);
        Member m2 = new Member("m2", 0, teamA);
        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        Page<MemberProjection> result = memberRepository.findByNativeProjection(PageRequest.of(0, 10));
        List<MemberProjection> content = result.getContent();

        for (MemberProjection memberProjection : content) {
            System.out.println("memberProjection.id = " + memberProjection.getId());
            System.out.println("memberProjection.userNamex = " + memberProjection.getUsername());
            System.out.println("memberProjection.teamName = " + memberProjection.getTeamName());

        }
    }


}