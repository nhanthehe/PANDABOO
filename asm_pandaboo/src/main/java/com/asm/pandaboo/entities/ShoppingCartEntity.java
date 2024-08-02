package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "shoppingcarts")
public class ShoppingCartEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private int cart_id;
	
	@OneToOne
	@JoinColumn(name = "acc_id",referencedColumnName = "acc_id")
	private AccountEntity cartAccountEntity;
	
	@OneToMany(mappedBy = "cartEntity")
	private List<PayDetailEntity> paydetails;

}
