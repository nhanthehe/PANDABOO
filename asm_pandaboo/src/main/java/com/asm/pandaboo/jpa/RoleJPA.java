package com.asm.pandaboo.jpa;

import com.asm.pandaboo.entities.AccountEntity;
import com.asm.pandaboo.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleJPA extends JpaRepository<RoleEntity, Integer>  {
    @Query(value = "SELECT * FROM roles WHERE role_name LIKE :role_name ", nativeQuery = true)
    RoleEntity getRoleByName(@Param("role_name") String role_name);
}
