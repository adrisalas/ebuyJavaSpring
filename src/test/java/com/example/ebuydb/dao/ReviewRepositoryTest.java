package com.example.ebuydb.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ReviewRepositoryTest {

     @Autowired
     private ReviewRepository reviewRepository;

     @Test
     public void correctAverageRatingWithMoreThan1Review(){
         assertEquals(4.25,reviewRepository.obtenerMediaValoraciones(4));
     }

     @Test
     public void correctAverageRatingWith1Review(){
         assertEquals(2.0,reviewRepository.obtenerMediaValoraciones(2));
     }

     @Test
    public void noAverageRating(){
         assertEquals(-1,reviewRepository.obtenerMediaValoraciones(6));
     }
}
