package org.inflearn._01_04_jpabook.service;

import org.inflearn._01_04_jpabook.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{

        Book book = em.find(Book.class, 1L);

        // TX
        book.setName("asdasd");

        // 변경감지 ==> dirty checking
        
        // TX commit


    }

}
