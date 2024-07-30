package com.asm.pandaboo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asm.pandaboo.entities.ProductEntity;
import com.asm.pandaboo.entities.ShoppingCartEntity;
import com.asm.pandaboo.jpa.ProductJPA;
import com.asm.pandaboo.jpa.ShoppingCartJPA;

@Service
public class ProductService {
	@Autowired
	ProductJPA prodJPA;
	
	@Autowired
	ShoppingCartJPA shoppingCartJPA;
	
	public List<ProductEntity> getProducts(){
		return prodJPA.findAll();
	}
	
	public ShoppingCartEntity getShoppingCarts(int cliID){
		return shoppingCartJPA.findShoppingCartByCliID(cliID);
	}
	
	public ProductEntity getProductById(int id) {
		List<ProductEntity> prodList = this.getProducts();
		for(ProductEntity product: prodList) {
			if(product.getProd_id() == id) {
				return product;
			}
		}
		return null;
	}
	
	public ShoppingCartEntity getCartByClientId(int cliID) {
		ShoppingCartEntity cart = this.getShoppingCarts(cliID);
		if(cart.getClientEntity().getCli_id() == cliID) {
			return cart;
		}else {
			return null;
		}		
	}
}
