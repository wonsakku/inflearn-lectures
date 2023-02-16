package hellojpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Product {

    @Id @GeneratedValue
    @Column(name = "product_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();

//    @ManyToMany(mappedBy = "products")
//    private List<Member> members = new ArrayList<>();

}
