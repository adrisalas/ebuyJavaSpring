package com.example.ebuydb.dao;

import com.example.ebuydb.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.accountByVendorId.userId = :id")
    List<Product> findByVendorId(@Param("id") Integer vendorId);

    @Query("SELECT p  FROM Product p WHERE p.subcategoryBySubcategoryId.categoryByCategoryId.categoryId = :codigo")
    List<Product> findByCategoria(@Param("codigo")Integer codigoCategoria);

    @Query("SELECT p  FROM Product p inner join p.productKeywordsByProductId k where k.keywordId = :codigo")
    List<Product> findByKeyword(@Param("codigo") Integer codigoKeyword);

    @Query("SELECT p FROM Product p WHERE p.creationDate = :date")
    List<Product> findByDate(@Param("date") Date date);

    @Query("SELECT p FROM Product p WHERE p.creationTime = :time")
    List<Product> findByTime(@Param("time") Date time);

    @Query("SELECT p FROM Product p WHERE UPPER(p.title) LIKE CONCAT('%',UPPER(:TITULO) ,'%')")
    List<Product> findByTitulo(@Param("TITULO") String titulo);

    @Query("SELECT p FROM Product p WHERE UPPER(p.description) LIKE CONCAT('%',UPPER(:DESCRIPCION) ,'%')")
    List<Product> findByDescripcion(@Param("DESCRIPCION") String descripcion);

    @Query("SELECT p FROM Product p ORDER BY p.creationDate desc, p.creationTime desc")
    List<Product> allProductsOrderByDate();
}
