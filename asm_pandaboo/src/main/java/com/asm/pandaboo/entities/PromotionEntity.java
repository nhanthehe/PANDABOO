package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "promotions")
public class PromotionEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prom_id")
	private int prom_id;
	
	@Column(name = "prom_name")
	private String prom_name;
	
	@Column(name = "start_price")
	private Double start_price;
	
	@Column(name = "start_date")
	private Date start_date;
	
	@Column(name = "end_date")
	private Date end_date;
	
	@Column(name = "discount")
	private Double discount;
	
	@Column(name = "note")
	private String note;
	
	@Column(name = "status")
	private boolean status;
	
	@OneToMany(mappedBy = "promotionEnity")
	private List<PaymentEntity> paymentEntity;

}
