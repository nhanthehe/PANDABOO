package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.PaymentDetailEntity;

public interface PaymentDetailJPA extends JpaRepository<PaymentDetailEntity, Integer> {
	@Query(value = "SELECT * FROM paymentdetails WHERE pay_id =:payId ",nativeQuery = true)
	public List<PaymentDetailEntity> getListPaymentDetailByPayId(@Param("payId") int payId);

}
