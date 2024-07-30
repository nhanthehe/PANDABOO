package com.asm.pandaboo.interfaces;

import java.util.List;

import com.asm.pandaboo.entities.PayDetailEntity;
import com.asm.pandaboo.entities.ShoppingCartEntity;


public interface PayService {
	List<PayDetailEntity> getCartList(int clientId);
	void add(int id, int clientId);
	void add(int id, int quantity, int clientId);
	void remove(int id, int clientId);
	void update(int id, int quantity, int clientId);
	void clear(int clientId);
	int getCount(int clientId);
	double getAmount(int clientId);
	
}
