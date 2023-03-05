package org.inflearn.repository;

import org.inflearn.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    void save(){
        Item item = new Item(UUID.randomUUID().toString());
        Item saved = itemRepository.save(item);
    }
}