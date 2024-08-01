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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "address")
public class AddressEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_id")
    private int add_id;

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
   	
   	@OneToOne(mappedBy = "addressEntity", cascade = CascadeType.ALL)
   	private ShoppingCartEntity carts;
   	
   	@OneToMany(mappedBy = "addressPaymentsEntity")
	private List<PaymentEntity> paymentsAddressEntity;

}
