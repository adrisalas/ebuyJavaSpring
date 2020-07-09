package com.example.ebuydb.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID", nullable = false)
    private int productId;

    @Basic
    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;

    @Basic
    @Column(name = "DESCRIPTION", nullable = true, length = 1020)
    private String description;

    @Basic
    @Column(name = "PRICE", nullable = true, precision = 0)
    private Double price;

    @Basic
    @Column(name = "PHOTO_URL", nullable = true, length = 510)
    private String photoUrl;

    @Basic
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

//    @Basic
//    @Column(name = "VENDOR_ID", nullable = false)
//    private int vendorId;

    @Basic
    @Column(name = "QUANTITY", nullable = false)
    private int quantity;

//    @Basic
//    @Column(name = "SUBCATEGORY_ID", nullable = false)
//    private int subcategoryId;

    @Basic
    @Column(name = "CREATION_TIME", nullable = false)
    private Time creationTime;

    @ManyToOne
    @JoinColumn(name = "VENDOR_ID", referencedColumnName = "USER_ID", nullable = false)
    private Account accountByVendorId;

    @ManyToOne
    @JoinColumn(name = "SUBCATEGORY_ID", referencedColumnName = "SUBCATEGORY_ID", nullable = false)
    private Subcategory subcategoryBySubcategoryId;

    @OneToMany(mappedBy = "productByProductId")
    private List<ProductKeyword> productKeywordsByProductId;

    @OneToMany(mappedBy = "productByProductId")
    private List<PurchasedProduct> purchasedProductsByProductId;
}
