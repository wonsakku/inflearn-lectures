package hellojpa_02;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Setter
@Getter
@Entity
@DiscriminatorValue(value = "M")
public class Movie extends Item{

    private String director;
    private String actor;

}
