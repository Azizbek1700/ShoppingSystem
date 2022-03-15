package uz.master.warehouse.entity;

import lombok.Getter;
import lombok.Setter;
import uz.master.warehouse.entity.base.Auditable;
import uz.master.warehouse.enums.Role;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class AuthUser extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private String picturePath;

    @Column(nullable = false)
    private Long orgId;

    @Column(nullable = false)
    private Role role;
}
