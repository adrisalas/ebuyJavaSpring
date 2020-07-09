package com.example.ebuydb.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID", nullable = false)
    private int reviewId;

//    @Basic
//    @Column(name = "PURCHASE_ID", nullable = false)
//    private int purchaseId;
//
//    @Basic
//    @Column(name = "USER_ID", nullable = false)
//    private int userId;

    @Basic
    @Column(name = "REVIEW_DATE", nullable = false)
    private Date reviewDate;

    @Basic
    @Column(name = "STARS", nullable = false, precision = 0)
    private double stars;

    @Basic
    @Column(name = "COMMENT", nullable = true, length = 510)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "PURCHASE_ID", referencedColumnName = "PURCHASE_ID", nullable = false)
    private PurchasedProduct purchasedProductByPurchaseId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    private Account accountByUserId;
}
