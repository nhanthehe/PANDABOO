package com.asm.pandaboo.models;

import org.springframework.stereotype.Service;

import com.asm.pandaboo.entities.ProductEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartBean extends ProductBean {
	private int quantity;
}
