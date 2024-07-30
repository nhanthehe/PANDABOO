package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "paydetails")
public class PayDetailEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int detailId;
	
	@Column(name="quantity")
	private int quantity;
	
	@ManyToOne
	@JoinColumn(name="cart_id")
	@JsonBackReference
	private ShoppingCartEntity cartEntity;
	
	@ManyToOne
	@JoinColumn(name="prod_id")
	@JsonBackReference
	private ProductEntity paydetailProductEntity;
	
}
