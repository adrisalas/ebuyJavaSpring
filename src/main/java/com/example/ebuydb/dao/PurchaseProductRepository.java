package com.example.ebuydb.dao;

import com.example.ebuydb.entity.PurchasedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseProductRepository extends JpaRepository<PurchasedProduct,Integer> {


    @Query("SELECT p FROM PurchasedProduct  p WHERE  p.accountByBuyerId.userId = :idUsuario order by  p.purchaseDate desc")
    List<PurchasedProduct> findByBuyerId(@Param("idUsuario") Integer id);
}
