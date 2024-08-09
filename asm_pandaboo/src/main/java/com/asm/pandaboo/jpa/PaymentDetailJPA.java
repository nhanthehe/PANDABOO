package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.PaymentDetailEntity;

public interface PaymentDetailJPA extends JpaRepository<PaymentDetailEntity, Integer> {
	@Query(value = "SELECT * FROM paymentdetails WHERE pay_id =:payId ",nativeQuery = true)
	public List<PaymentDetailEntity> getListPaymentDetailByPayId(@Param("payId") int payId);

	@Query(value = "SELECT paydetail_id, pd.pay_id, prod_id, " +
			"prod_name, prod_quantity, red_price, prod_images " +
			"FROM paymentdetails pd " +
			"INNER JOIN payments p on p.pay_id = pd.pay_id " +
			"INNER JOIN accounts a on a.acc_id = p.acc_id " +
			"WHERE p.acc_id = :accId", nativeQuery = true)
	public List<PaymentDetailEntity> getListPaymentDetailByAccId(@Param("accId") int accId);
}
