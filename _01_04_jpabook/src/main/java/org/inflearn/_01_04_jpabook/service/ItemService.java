package org.inflearn._01_04_jpabook.service;

import lombok.RequiredArgsConstructor;
import org.inflearn._01_04_jpabook.domain.item.Item;
import org.inflearn._01_04_jpabook.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {


    private final ItemRepository itemRepository;


    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long id){
        return itemRepository.findOne(id);
    }


}





