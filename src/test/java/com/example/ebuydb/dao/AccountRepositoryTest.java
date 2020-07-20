package com.example.ebuydb.dao;


import com.example.ebuydb.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void sucessFindByEmail(){
        Account account = accountRepository.findByEmail("admin@ebuy.com");
        assertEquals(1,account.getUserId());
        assertEquals("admin",account.getNickname());
        assertEquals("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918",account.getPassword());
        assertEquals(1,account.getIsadmin());
        assertEquals("admin@ebuy.com",account.getEmail());
    }
    @Test
    public void incorrectFindByEmail(){
        Account account = accountRepository.findByEmail("doesNot@email.com");
        assertEquals(null,account);
    }
}
