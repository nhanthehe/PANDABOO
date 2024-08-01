package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.AccountEntity;

public interface AccountsJPA extends JpaRepository<AccountEntity, String> {

//	@Query(value = "SELECT * FROM accounts WHERE username LIKE :username ", nativeQuery = true)
//	AccountEntity getAccountsEntityByAcc(@Param("username") String username);
@Query(value = "SELECT acc.*, r.role_name FROM accounts acc " +
		"INNER JOIN role_accounts ra ON acc.acc_id = ra.acc_id " +
		"INNER JOIN roles r ON ra.role_id = r.role_id " +
		"WHERE acc.username = :username", nativeQuery = true)
AccountEntity getAccountsEntityByAcc(@Param("username") String username);



	@Query(value = "SELECT * FROM accounts WHERE acc_id LIKE :acc_id ", nativeQuery = true)
	AccountEntity getAccountsByAccId(@Param("acc_id") int acc_id);
}
