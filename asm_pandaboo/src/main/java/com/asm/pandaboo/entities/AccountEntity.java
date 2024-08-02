package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class AccountEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acc_id")
    private int acc_id;
    
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;
    
    @Column(name = "avatar")
    private String avatar;
    
    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "roleAccountAccEntity")
    private List<RoleAccountEntity> accRoleAccounts;
    
    @OneToOne(mappedBy = "accountEntity", cascade = CascadeType.ALL)
    private AddressEntity addressEntity;

    @OneToOne(mappedBy = "cartAccountEntity", cascade = CascadeType.ALL)
   	private ShoppingCartEntity accountCart;

   	@OneToMany(mappedBy = "accountPaymentsEntity")
	private List<PaymentEntity> paymentsAccountEntity;
}
