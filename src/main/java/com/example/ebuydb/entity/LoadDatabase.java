package com.example.ebuydb.entity;

import com.example.ebuydb.dao.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(AccountRepository accountRepository) {
        return args -> {
            log.info("Preloading " + accountRepository.save(new Account(0,"nick","email","pass",(short)0)));
            log.info("Preloading " + accountRepository.save(new Account(0,"nick2","email2","pass",(short)0)));
            log.info("Preloading " + accountRepository.save(new Account(0,"nick3","email3","pass",(short)0)));
        };
    }
}
