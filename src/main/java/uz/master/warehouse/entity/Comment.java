package uz.master.warehouse.entity;

import lombok.Getter;
import lombok.Setter;
import uz.master.warehouse.entity.base.Auditable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Comment extends Auditable {
    private Long ClientBarId;
    private Long authorId;
    private String text;
}
