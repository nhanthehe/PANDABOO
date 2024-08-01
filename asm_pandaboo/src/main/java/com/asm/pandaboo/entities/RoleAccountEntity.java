package com.asm.pandaboo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "role_accounts")
public class RoleAccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_account_id")
    private int role_account_id;

    @ManyToOne
    @JoinColumn(name = "acc_id")
    @JsonBackReference
    private AccountEntity roleAccountAccEntity;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private RoleEntity roleAccountRoleEntity;
}
