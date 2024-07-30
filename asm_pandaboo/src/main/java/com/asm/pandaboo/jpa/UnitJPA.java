package com.asm.pandaboo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm.pandaboo.entities.UnitEntity;


public interface UnitJPA extends JpaRepository<UnitEntity, String> {

}
