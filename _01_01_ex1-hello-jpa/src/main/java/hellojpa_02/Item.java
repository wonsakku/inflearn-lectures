package hellojpa_02;


import lombok.*;

import javax.persistence.*;


@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // default -> 컬럼명은 dtype, 값은 entity명
public abstract class Item {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private int price;


}
