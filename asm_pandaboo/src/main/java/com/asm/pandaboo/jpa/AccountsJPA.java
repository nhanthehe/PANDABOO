package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.AccountEntity;

public interface AccountsJPA extends JpaRepository<AccountEntity, String> {

	@Query(value = "SELECT * FROM accounts WHERE username LIKE :username ", nativeQuery = true)
	AccountEntity getAccountsEntityByAcc(@Param("username") String username);
}
