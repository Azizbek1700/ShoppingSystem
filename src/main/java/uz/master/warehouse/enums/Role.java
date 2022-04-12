package uz.master.warehouse.enums;

import lombok.Getter;

@Getter
public enum Role {
    SUPER_ADMIN,
    ADMIN,
    CASHIER,
    WAREHOUSEMAN,
    SELLER;

    public Role checkRole(String role){
        for (Role value : values()) {
            if (value.name().equalsIgnoreCase(role)){
                return value;
            }
        }
        throw new RuntimeException("role not found");
    }
}
