package com.asm.pandaboo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.AddressEntity;

public interface ClientJPA extends JpaRepository<AddressEntity, Integer> {
//	@Query(value = "SELECT * FROM accounts WHERE acc_id=:accId", nativeQuery = true)
//	public AddressEntity getAccountByAccID(@Param("accId") int accId);

}
