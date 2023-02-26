package org.inflearn._01_04_jpabook.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.inflearn._01_04_jpabook.domain.Category;
import org.inflearn._01_04_jpabook.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // ==비즈니스 로직== //

    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;

        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }

        this.stockQuantity = restStock;
    }

}





