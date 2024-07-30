package com.asm.pandaboo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.asm.pandaboo.entities.ImageEntity;

public interface ImageJPA extends JpaRepository<ImageEntity, String> {

}
