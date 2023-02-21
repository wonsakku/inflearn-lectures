package hellojpa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member  extends BaseEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "name")
//    private String username;
//    private Integer age;
//
//    @Enumerated(EnumType.STRING)
//    private RoleType roleType;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date createdDate;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date lastModifiedDate;
//
//    @Lob
//    private String description;
//
//    @Transient // DB 컬럼으로 매핑되지 않음
//    private int temp;
//
//
//    public Member(Long id, String username) {
//        this.id = id;
//        this.username = username;
//    }

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

//    @Column(name = "team_id")
//    private Long teamId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToOne
    @JoinColumn(name = "locker_id")
    private Locker locker;

//    @ManyToMany
//    @JoinTable(name = "member_product")
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();

    @Embedded
    private Address homeAddress;

    @ElementCollection
    @CollectionTable(name = "favorite_food", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "food_name")
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name = "address", joinColumns = @JoinColumn(name = "member_id"))
//    private List<Address> addressHistory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "work_city")),
            @AttributeOverride(name = "street", column = @Column(name = "work_street")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "work_zipcode"))
    })
    private Address workAddress;

    @Embedded
    private Period workPeriod;



    public Member(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public void changeTeam(Team team){
        this.team = team;
        this.team.addMember(this);
    }

}


















