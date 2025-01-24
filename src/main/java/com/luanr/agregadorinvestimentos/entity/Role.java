package com.luanr.agregadorinvestimentos.entity;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "roles_tb")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, updatable = false, unique = true)
    private Long roleId;

    @Column(name = "role_name", nullable = false)
    private String roleName;

    @Getter
    public enum Values {

        ADMIN(1L),
        BASIC(2L);

        final long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }

    }
}
