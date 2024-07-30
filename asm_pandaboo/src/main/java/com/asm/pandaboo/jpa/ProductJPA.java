package com.asm.pandaboo.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.asm.pandaboo.entities.ProductEntity;

public interface ProductJPA extends JpaRepository<ProductEntity, String> {
    @Query(value = "SELECT * FROM products WHERE prod_id=:prod_id", nativeQuery = true)
    public ProductEntity getProductByID(@Param("prod_id") String prod_id);
    
    @Query(value = "SELECT * FROM products WHERE cat_id=:cat_id", nativeQuery = true)
    Page<ProductEntity> getProductByCatID(@Param("cat_id") String cat_id, Pageable pageable);
    
    @Query(value = "SELECT * FROM products WHERE prod_name LIKE :prodName", nativeQuery = true)
    Page<ProductEntity> getProductByProdName(@Param("prodName") String prodName, Pageable pageable);
    
    @Query(value = "SELECT SUM(prod_quantity) FROM paymentdetails WHERE prod_id = :prod_id", nativeQuery = true)
    public Integer getSoldById(@Param("prod_id") String prod_id);
    
//    @Query(value = "select COUNT(prod_id) as sold from paymentdetails "
//    		+ "group by prod_id", nativeQuery = true)
//    public Integer getSold();
}
