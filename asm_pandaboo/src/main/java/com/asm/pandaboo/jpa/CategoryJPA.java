package com.asm.pandaboo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm.pandaboo.entities.CategoryEntity;

public interface CategoryJPA extends JpaRepository<CategoryEntity, String> {

}
