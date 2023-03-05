package org.inflearn.controller;

import lombok.RequiredArgsConstructor;
import org.inflearn.dto.MemberDto;
import org.inflearn.entity.Member;
import org.inflearn.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init(){
        for(int i = 0 ; i < 100 ; i++){
            memberRepository.save(new Member("user_" + i, i));
        }
    }

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        return memberRepository.findAll(pageable);
    }

    @GetMapping("/members2")
    public Page<Member> list2(@PageableDefault(size = 5, page = 0) Pageable pageable){
        return memberRepository.findAll(pageable);
    }


    @GetMapping("/members3")
    public Page<MemberDto> list3(@PageableDefault(size = 5, page = 0) Pageable pageable){
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }

}
