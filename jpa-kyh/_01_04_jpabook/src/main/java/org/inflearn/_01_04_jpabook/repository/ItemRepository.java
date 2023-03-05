package org.inflearn._01_04_jpabook.repository;

import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.item.Item;
import org.springframework.stereotype.Repository;
import org.thymeleaf.expression.Lists;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){
            em.persist(item);
        }else{
//            Item merge = em.merge(item);
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("SELECT i FROM Item i", Item.class)
                .getResultList();
    }


}
