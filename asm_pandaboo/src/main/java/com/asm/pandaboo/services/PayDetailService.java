package com.asm.pandaboo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asm.pandaboo.entities.PayDetailEntity;
import com.asm.pandaboo.entities.ProductEntity;
import com.asm.pandaboo.entities.ShoppingCartEntity;
import com.asm.pandaboo.interfaces.PayService;
import com.asm.pandaboo.jpa.ClientJPA;
import com.asm.pandaboo.jpa.PayDetailJPA;
import com.asm.pandaboo.jpa.ShoppingCartJPA;

@Service
public class PayDetailService implements PayService {
	@Autowired
	ProductService prodService;

	@Autowired
	ClientJPA clientJPA;

	@Autowired
	ShoppingCartJPA cartJPA;

	@Autowired
	PayDetailJPA payDetailJPA;

	@Override
	public List<PayDetailEntity> getCartList(int clientId) {
		List<PayDetailEntity> carts = payDetailJPA.getFindByAccId(String.valueOf(clientId));
		return carts == null ? new ArrayList<PayDetailEntity>() : carts;
	}

	@Override
	public void add(int id, int clientId) {
		List<PayDetailEntity> carts = this.getCartList(clientId);
		int index = -1;
		for (int indexCart = 0; indexCart < carts.size(); indexCart++) {
			if (id == carts.get(indexCart).getPaydetailProductEntity().getProd_id()) {
				index = indexCart;
				break;
			}
		}
		ProductEntity prodEntity = prodService.getProductById(id);
		if (prodEntity == null) {
			throw new IllegalArgumentException("Product not found with id: " + id);
		}

		ShoppingCartEntity shoppingCartEntity = prodService.getCartByAccId(clientId);
		if (shoppingCartEntity == null) {
            throw new IllegalArgumentException("Shopping cart not found for client id: " + clientId);
        }

		if (index == -1) {
			PayDetailEntity payItem = new PayDetailEntity();
			payItem.setPaydetailProductEntity(prodEntity);
			payItem.setCartEntity(shoppingCartEntity);
			payItem.setQuantity(1);
			carts.add(payItem);
			payDetailJPA.save(payItem);
		} else {
			PayDetailEntity payItem = carts.get(index);
			int quantity = payItem.getQuantity();
			payItem.setQuantity(quantity + 1);
			carts.set(index, payItem);
			payDetailJPA.save(payItem);
		}

	}

	@Override
	public void add(int id, int quantity, int clientId) {
		List<PayDetailEntity> carts = this.getCartList(clientId);
		int index = -1;
		for (int indexCart = 0; indexCart < carts.size(); indexCart++) {
			if (id == carts.get(indexCart).getPaydetailProductEntity().getProd_id()) {
				index = indexCart;
				break;
			}
		}
		ProductEntity prodEntity = prodService.getProductById(id);
		if (prodEntity == null) {
			throw new IllegalArgumentException("Product not found with id: " + id);
		}

		ShoppingCartEntity shoppingCartEntity = prodService.getCartByAccId(clientId);
		if (shoppingCartEntity == null) {
            throw new IllegalArgumentException("Shopping cart not found for client id: " + clientId);
        }

		if (index == -1) {
			PayDetailEntity payItem = new PayDetailEntity();
			payItem.setPaydetailProductEntity(prodEntity);
			payItem.setCartEntity(shoppingCartEntity);
			payItem.setQuantity(quantity);
			carts.add(payItem);
			payDetailJPA.save(payItem);
		} else {
			PayDetailEntity payItem = carts.get(index);
			int quantityIndex = payItem.getQuantity();
			payItem.setQuantity(quantityIndex + quantity);
			carts.set(index, payItem);
			payDetailJPA.save(payItem);
		}

	}

	@Override
	public void remove(int id, int clientId) {
		List<PayDetailEntity> carts = this.getCartList(clientId);
		int index = -1;
		for (int indexCart = 0; indexCart < carts.size(); indexCart++) {
			if (id == carts.get(indexCart).getPaydetailProductEntity().getProd_id()) {
				index = indexCart;
				break;
			}
		}
		if(index!=-1) {
			PayDetailEntity payItem = carts.get(index);
			payDetailJPA.delete(payItem);
			carts.remove(index);
		}

	}

	@Override
	public void update(int id, int quantity, int clientId) {
		List<PayDetailEntity> carts = this.getCartList(clientId);
		for (int indexCart = 0; indexCart < carts.size(); indexCart++) {
			if (id == carts.get(indexCart).getPaydetailProductEntity().getProd_id()) {
				PayDetailEntity payItem = carts.get(indexCart);
				payItem.setQuantity(quantity);
				carts.set(indexCart, payItem);
				payDetailJPA.save(payItem);
				break;
			}
		}
	}

	@Override
	public void clear(int clientId) {
		List<PayDetailEntity> carts = this.getCartList(clientId);

		carts.clear();
	}

	@Override
	public int getCount(int clientId) {
		int count = 0;
		for (PayDetailEntity payDetail : this.getCartList(clientId)) {
			count +=payDetail.getQuantity();
		}
		return count;
	}

	@Override
	public double getAmount(int clientId) {
		int amount = 0;
		for (PayDetailEntity payDetail : this.getCartList(clientId)) {
			amount +=payDetail.getPaydetailProductEntity().getRed_price() * payDetail.getQuantity();
		}
		return amount;
	}

}
