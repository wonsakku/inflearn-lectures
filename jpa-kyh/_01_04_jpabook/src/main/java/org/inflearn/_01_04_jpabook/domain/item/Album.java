package org.inflearn._01_04_jpabook.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@DiscriminatorValue(value = "A")
public class Album extends Item{
    private String artist;
    private String etc;



}
