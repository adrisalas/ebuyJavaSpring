package com.example.ebuydb.dao;

import com.example.ebuydb.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account,Integer> {

    @Query("SELECT a FROM Account a WHERE a.email = :email")
    Account findByEmail(@Param("email") String email);

    @Query("SELECT a FROM Account a WHERE a.nickname = :nickname")
    Account findByNickname(@Param("nickname") String nickname);
}
