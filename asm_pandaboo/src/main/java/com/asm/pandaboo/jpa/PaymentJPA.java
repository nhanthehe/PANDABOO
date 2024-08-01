package com.asm.pandaboo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.PaymentEntity;

public interface PaymentJPA extends JpaRepository<PaymentEntity, String> {
	@Query(value = "select * from payments where acc_id=:cliId",nativeQuery = true)
	public PaymentEntity getPaymentByClientID(@Param("cliId") int cliId);
	
	@Query(value = "select * from payments where acc_id=:cliId",nativeQuery = true)
	public List<PaymentEntity> getListPaymentByClientID(@Param("cliId") int cliId);
	
	@Query(value = "SELECT SUM(order_total) FROM payments WHERE status = 2", nativeQuery = true)
    public Integer getSoldTotal();
	
	@Query(value = "SELECT SUM(prod_quantity) FROM paymentdetails pd INNER JOIN payments p ON p.pay_id =pd.pay_id "
			+ "WHERE p.status = 2", nativeQuery = true)
    public Integer getSoldAll();
	
	@Query(value = "select COUNT(DISTINCT acc.acc_id) from payments p INNER JOIN accounts acc ON p.acc_id = acc.acc_id WHERE p.status = 2", nativeQuery = true)
	public Integer getCliAll();
	
	@Query(value = "SELECT TOP 3 pd.prod_name, SUM(pd.prod_quantity * pd.red_price) AS total, SUM(prod_quantity) AS countProd FROM paymentdetails pd "
			+ "INNER JOIN payments p ON p.pay_id =pd.pay_id WHERE p.status = 2 "
			+ "GROUP BY prod_name ORDER BY total DESC", nativeQuery = true)
    public List<Object[]> getTop3ProductTotal();

}
