package com.asm.pandaboo.interfaces;

import java.util.List;

import com.asm.pandaboo.entities.PayDetailEntity;
import com.asm.pandaboo.entities.ShoppingCartEntity;


public interface PayService {
	List<PayDetailEntity> getCartList(int accId);
	void add(int id, int accId);
	void add(int id, int quantity, int accId);
	void remove(int id, int accId);
	void update(int id, int quantity, int accId);
	void clear(int accId);
	int getCount(int accId);
	double getAmount(int accId);
	
}
