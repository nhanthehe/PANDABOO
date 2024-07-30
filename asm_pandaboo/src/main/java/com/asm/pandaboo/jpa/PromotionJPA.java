package com.asm.pandaboo.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.PromotionEntity;

public interface PromotionJPA extends JpaRepository<PromotionEntity, Integer>{
	@Query(value = "SELECT * FROM promotions p WHERE p.status = 1 AND p.start_date <= :date AND p.end_date >= :date", nativeQuery = true)
    public List<PromotionEntity> findAllActivePromotions(@Param("date") Date date);
}
