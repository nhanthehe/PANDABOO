package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class ClientEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cli_id")
    private int cli_id;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "road")
    private String road;
    
    @Column(name = "ward")
    private String ward;
    
    @Column(name = "district")
    private String district;
    
    @Column(name = "city")
    private String city;

    @Column(name = "status")
    private boolean status;

    @OneToOne
    @JoinColumn(name = "acc_id", referencedColumnName = "acc_id")
    private AccountEntity accountEntity;
   	
   	@OneToOne(mappedBy = "clientEntity", cascade = CascadeType.ALL)
   	private ShoppingCartEntity carts;
   	
   	@OneToMany(mappedBy = "clientPaymentsEntity")
	private List<PaymentEntity> paymentsClientEntity;

}
