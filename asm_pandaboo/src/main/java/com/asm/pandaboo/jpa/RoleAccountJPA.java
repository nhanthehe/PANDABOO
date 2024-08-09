package com.asm.pandaboo.jpa;

import com.asm.pandaboo.entities.AccountEntity;
import com.asm.pandaboo.entities.RoleAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleAccountJPA extends JpaRepository<RoleAccountEntity, Integer> {
    @Query(value = "SELECT * FROM role_accounts WHERE acc_id = :acc_id ", nativeQuery = true)
    List<RoleAccountEntity> getRoleAccountByAccId(@Param("acc_id") int acc_id);
}
