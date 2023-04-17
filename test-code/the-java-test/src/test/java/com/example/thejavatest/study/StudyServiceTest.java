package com.example.thejavatest.study;

import com.example.thejavatest.domain.Member;
import com.example.thejavatest.domain.Study;
import com.example.thejavatest.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {
    @Mock MemberService memberService;
    @Mock StudyRepository studyRepository;

    @Test
    @DisplayName("Mock 객체 Stubbing")
    void createNewStudy(@Mock MemberService memberService, @Mock StudyRepository studyRepository){
//        final MemberService memberService = Mockito.mock(MemberService.class);
//        final StudyRepository studyRepository = Mockito.mock(StudyRepository.class);
        final StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@email.com");

        // stubbing
//        Mockito.when(memberService.findById(any())).thenReturn(Optional.of(member));
        Mockito.when(memberService.findById(1l))
                .thenReturn(Optional.of(member))
                .thenThrow(new RuntimeException())
                .thenReturn(Optional.empty());
        Mockito.doThrow(new IllegalArgumentException()).when(memberService).validate(1L);

        final Optional<Member> findById = memberService.findById(1L);
        assertEquals("test@email.com", findById.get().getEmail());

        assertThrows(RuntimeException.class, () -> {
            memberService.findById(1L);
        });

        assertEquals(Optional.empty(), memberService.findById(1L));

        assertThrows(IllegalArgumentException.class, () -> {
            memberService.validate(1L);
        });

        memberService.validate(2L);
    }


    @Test
    @DisplayName("Mock 객체 Stubbing 연습 문제")
    void mockStubbing(){
        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@email.com");

        final Study study = new Study(10, "테스트");

        Mockito.when(memberService.findById(1L)).thenReturn(Optional.of(member));
        Mockito.when(studyRepository.save(study)).thenReturn(study);

        final StudyService studyService = new StudyService(memberService, studyRepository);
        studyService.createNewStudy(1L, study);

        assertNotNull(study.getOwner());
        assertEquals(member, study.getOwner());
    }

    @Test
    @DisplayName("Mock 객체 확인")
    void mockObjectCheck(){

        // given
        final StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@email.com");

        final Study study = new Study(10, "테스트");

        Mockito.when(memberService.findById(1L)).thenReturn(Optional.of(member));
        Mockito.when(studyRepository.save(study)).thenReturn(study);

        // when
        studyService.createNewStudy(1L, study);

        // then
        assertEquals(member, study.getOwner());
//        Mockito.verify(memberService, Mockito.times(1)).notify(study);
        Mockito.verify(memberService, Mockito.times(1)).notify(study);

//        Mockito.verifyNoMoreInteractions(memberService);

        Mockito.verify(memberService, Mockito.times(1)).notify(member);
        Mockito.verify(memberService, Mockito.never()).validate(any());

        final InOrder inOrder = Mockito.inOrder(memberService);
        inOrder.verify(memberService).notify(study);
        inOrder.verify(memberService).notify(member);
    }

    @Test
    @DisplayName("BDD")
    void createNewStudy_BDD(){
        // given
        final StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("test@email.com");

        final Study study = new Study(10, "테스트");

        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // when
        studyService.createNewStudy(1L, study);

        // then
        assertEquals(member, study.getOwner());
        then(memberService).should(Mockito.times(1)).notify(study);
//        then(memberService).shouldHaveNoMoreInteractions();
    }

    @DisplayName("다른 사용자가 볼 수 있도록 스터디를 공개한다.")
    @Test
    void openStudy(){
        //given
        final StudyService studyService = new StudyService(memberService, studyRepository);
        final Study study = new Study(10, "더 자바, 테스트");
        assertNull(study.getOpenedDateTime());

        // TODO studyRepository Mock 객체의 save 메소드 호출 시 study를 리턴하도록 만들기
        given(studyRepository.save(study)).willReturn(study);

        // when
        studyService.openStudy(study);

        // then
        // TODO study의 status가 OPENED로 변경됐는지 확인
        assertEquals(StudyStatus.OPENED, study.getStatus());
        // TODO study의 openDateTime이 null이 아닌지 확인
        assertNotNull(study.getOpenedDateTime());
        // TODO memberService의 notify(study) 가 호출되었는지 확인
        then(memberService).should().notify(study);
    }

}




