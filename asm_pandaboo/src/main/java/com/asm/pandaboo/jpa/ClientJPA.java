package com.asm.pandaboo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.ClientEntity;

public interface ClientJPA extends JpaRepository<ClientEntity, Integer> {
	@Query(value = "SELECT * FROM clients WHERE acc_id=:accId", nativeQuery = true)
	public ClientEntity getClientByAccID(@Param("accId") int accId);

}
