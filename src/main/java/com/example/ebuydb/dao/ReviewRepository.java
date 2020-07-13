package com.example.ebuydb.dao;

import com.example.ebuydb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer>, ReviewRepositoryCustom {

    @Query("SELECT r FROM Review r WHERE r.purchaseId.purchaseId= :idCompra")
    List<Review> findByCompraId(@Param("idCompra")Integer id);

    @Query("SELECT r FROM Review r WHERE r.purchaseId.productByProductId.productId = :idProducto")
    List<Review> findByProductoId(@Param("idProducto")Integer id);
}
