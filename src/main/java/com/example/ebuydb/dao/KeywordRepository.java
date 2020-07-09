package com.example.ebuydb.dao;

import com.example.ebuydb.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword,Integer> {


    @Query("SELECT k.name FROM Keyword k")
    List<String> findAllName();

    @Query("SELECT k FROM Keyword k WHERE k.name = :kName")
    List<Keyword> findByName(@Param("kName")String name);
}
