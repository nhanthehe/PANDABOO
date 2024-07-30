package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "paymentdetails")
public class PaymentDetailEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "paydetail_id")
	private int paydetail_id;

	@ManyToOne
	@JoinColumn(name = "pay_id")
	@JsonBackReference
	private PaymentEntity paymentPaymentDetailEntity;
	
	@Column(name = "prod_id")
	private int prod_id;
	
	@Column(name = "prod_name")
	private String prod_name;

	@Column(name = "prod_quantity")
	private int prod_quantity;
	
	@Column(name = "red_price")
	private double red_price;
	
	@Column(name = "prod_images")
	private String prod_images;


}
