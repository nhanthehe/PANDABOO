package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.PayDetailEntity;

public interface PayDetailJPA extends JpaRepository<PayDetailEntity, String> {
	@Query(value = "select p.* from paydetails p "
			+ "inner join shoppingcarts s on s.cart_id=p.cart_id "
			+ "inner join clients c on c.cli_id = s.cli_id "
			+ "where c.cli_id = :cli_id",nativeQuery = true)
	public List<PayDetailEntity> getFindByCliId(@Param("cli_id") int id);
	
	
	@Query(value = "SELECT * FROM paydetails WHERE cart_id =:cartId",nativeQuery = true)
	public List<PayDetailEntity> getPayDetailByCartID(@Param("cartId") int cartId);
}
