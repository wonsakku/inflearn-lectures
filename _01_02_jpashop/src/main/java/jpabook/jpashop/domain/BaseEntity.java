package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Setter
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "insert_member")
    private String createdBy;
    private LocalDateTime createdDate;
    @Column(name = "update_member")
    private String modifiedBy;
    private LocalDateTime lastModifiedDate;

}
