package com.asm.pandaboo.entities;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "products")
public class ProductEntity implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prod_id")
	private int prod_id;

	@Column(name = "prod_name")
	private String prod_name;

	@ManyToOne
	@JoinColumn(name = "cat_id")
	@JsonIgnoreProperties(value = "products")
	private CategoryEntity categoryEntity;

	@ManyToOne
	@JoinColumn(name = "unit_id")
	@JsonIgnoreProperties(value = "products")
	private UnitEntity unitEntity;

	@Column(name = "price")
	private double price;

	@Column(name = "red_price")
	private double red_price;

	@Column(name = "descriptions")
	private String descriptions;

	@Column(name = "status")
	private boolean status;

	@OneToMany(mappedBy = "imageProductEntity")
	private List<ImageEntity> images;

	@OneToMany(mappedBy = "paydetailProductEntity")
	private List<PayDetailEntity> paydetails;

}
