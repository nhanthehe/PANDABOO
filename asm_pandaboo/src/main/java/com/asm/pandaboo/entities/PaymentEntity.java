package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "payments")
public class PaymentEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "pay_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int payId;

	@Column(name = "pay_date")
	private Date payDate;

	@Column(name = "total_quantity")
	private int totalQuantity;

	@Column(name = "order_total")
	private Double orderTotal;

	@Column(name = "pay_methods")
	private String payMethod;

	@Column(name = "status")
	private int status;

	@ManyToOne
	@JoinColumn(name = "cli_id")
	@JsonBackReference
	private ClientEntity clientPaymentsEntity;

	@ManyToOne
	@JoinColumn(name = "prom_id")
	@JsonBackReference
	private PromotionEntity promotionEnity;
	
	@OneToMany(mappedBy = "paymentPaymentDetailEntity")
	private List<PaymentDetailEntity> paymentdetailPaymentEntity;

}
