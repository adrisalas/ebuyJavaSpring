package com.example.ebuydb.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
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

    @JoinColumn(name = "PURCHASE_ID", referencedColumnName = "PURCHASE_ID")
    @OneToOne(optional = false)
    private PurchasedProduct purchaseId;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", nullable = false)
    private Account accountByUserId;
}
