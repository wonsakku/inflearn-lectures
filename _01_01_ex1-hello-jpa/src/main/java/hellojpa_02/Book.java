package hellojpa_02;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Setter
@Getter
@Entity
@DiscriminatorValue(value = "B")
public class Book extends Item {

    private String author;
    private String isbn;
}
