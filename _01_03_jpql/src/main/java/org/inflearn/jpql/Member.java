package org.inflearn.jpql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString(exclude = {"team"})
@Setter
@Getter
@Entity
public class Member {

    @Id @GeneratedValue
    private Long id;

    private String username;

    private int age;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private MemberType type;

    public void changeTeam(Team team){
        this.team = team;
        this.team.getMembers().add(this);
    }
}


