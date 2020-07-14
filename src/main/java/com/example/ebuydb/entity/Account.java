package com.example.ebuydb.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID", nullable = false)
    private int userId;

    @Basic
    @Column(name = "NICKNAME", nullable = false, length = 20)
    private String nickname;

    @Basic
    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Basic
    @Column(name = "PASSWORD", nullable = false, length = 64)
    private String password;

    @Basic
    @Column(name = "ISADMIN", nullable = false)
    private short isadmin;

    @OneToMany(mappedBy = "accountByVendorId")
    private List<Product> productsByUserId;

    @OneToMany(mappedBy = "accountByBuyerId")
    private List<PurchasedProduct> purchasedProductsByUserId;

    @OneToMany(mappedBy = "accountByUserId")
    private List<Review> reviewsByUserId;
}
