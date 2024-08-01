package com.asm.pandaboo.jpa;

import com.asm.pandaboo.entities.RoleAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleAccountJPA extends JpaRepository<RoleAccountEntity, Integer> {
}
