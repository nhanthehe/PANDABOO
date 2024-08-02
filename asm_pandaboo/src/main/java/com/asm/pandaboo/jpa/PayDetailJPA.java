package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.PayDetailEntity;

public interface PayDetailJPA extends JpaRepository<PayDetailEntity, String> {
	@Query(value = "select p.* from paydetails p "
			+ "inner join shoppingcarts s on s.cart_id=p.cart_id "
			+ "inner join accounts a on a.acc_id = s.acc_id "
			+ "where c.acc_id = :acc_id",nativeQuery = true)
	public List<PayDetailEntity> getFindByAccId(@Param("acc_id") String id);
	
	
	@Query(value = "SELECT * FROM paydetails WHERE cart_id =:cartId",nativeQuery = true)
	public List<PayDetailEntity> getPayDetailByCartID(@Param("cartId") int cartId);
}
