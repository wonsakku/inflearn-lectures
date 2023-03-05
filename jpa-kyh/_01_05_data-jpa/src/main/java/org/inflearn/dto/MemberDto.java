package org.inflearn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.inflearn.entity.Member;

@Data
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;


    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
