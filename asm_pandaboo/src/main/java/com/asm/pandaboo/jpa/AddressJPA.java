package com.asm.pandaboo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm.pandaboo.entities.AddressEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressJPA extends JpaRepository<AddressEntity, Integer> {
	@Query(value = "SELECT ad.* FROM address ad " +
            "INNER JOIN accounts ac ON ad.add_id = ac.acc_id " +
            "WHERE ac.acc_id = :accId", nativeQuery = true)
	public AddressEntity getAccountByAccID(@Param("accId") int accId);

}
