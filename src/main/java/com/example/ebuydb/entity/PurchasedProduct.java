package com.example.ebuydb.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "purchased_product")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PurchasedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PURCHASE_ID", nullable = false)
    private int purchaseId;

//    @Basic
//    @Column(name = "PRODUCT_ID", nullable = false)
//    private int productId;
//
//    @Basic
//    @Column(name = "BUYER_ID", nullable = false)
//    private int buyerId;

    @Basic
    @Column(name = "PURCHASE_DATE", nullable = false)
    private Date purchaseDate;

    @Basic
    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

    @Basic
    @Column(name = "PRICE", nullable = false, precision = 0)
    private double price;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", nullable = false)
    private Product productByProductId;

    @ManyToOne
    @JoinColumn(name = "BUYER_ID", referencedColumnName = "USER_ID", nullable = false)
    private Account accountByBuyerId;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "purchaseId")
    private Review review;
}
