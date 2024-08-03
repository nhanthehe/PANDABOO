package com.asm.pandaboo.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.ShoppingCartEntity;

public interface ShoppingCartJPA extends JpaRepository<ShoppingCartEntity, String> {
	@Query(value = "SELECT * FROM shoppingcarts WHERE acc_id=:cliID",nativeQuery = true)
	public ShoppingCartEntity findShoppingCartByCliID(@Param("cliID") String cliID);
}
