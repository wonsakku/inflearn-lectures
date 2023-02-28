package org.inflearn._01_04_jpabook.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.Member;
import org.inflearn._01_04_jpabook.service.MemberService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    @GetMapping("/api/v1/members")
    public List<Member> membersV1(){
        // 양방향 참조 -> 참조 연쇄? -> stackOverflow?
        // entity 정보 전부 노출됨
        return memberService.findMembers();
    }


    @GetMapping("/api/v2/members")
    public Result membersV2(){
        List<MemberDto> result = memberService.findMembers().stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());
        return new Result(result.size(), result);
    }


    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Validated Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Validated CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{memberId}")
    public UpdateMemberResponse updateMemberV2(@RequestBody @Validated UpdateMemberRequest request,
                                               @PathVariable Long memberId){

        // cammnd와 query 분리 -> 반환값이 없거나 id 정도만 반환
        Long id = memberService.update(memberId, request.getName());
        Member findMember = memberService.findOne(id);
        ////////////////////////////////////////////////////////////

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class MemberDto{
        private String name;

    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateMemberResponse{
        private Long id;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class UpdateMemberRequest{
        private Long id;
        private String name;

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class CreateMemberRequest {
        private String name;
    }
}
