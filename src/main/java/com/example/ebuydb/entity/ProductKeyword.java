package com.example.ebuydb.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_keyword")
@IdClass(ProductKeywordPK.class)
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ProductKeyword {
    @Id
    @Column(name = "KEYWORD_ID", nullable = false)
    private int keywordId;

    @Id
    @Column(name = "PRODUCT_ID", nullable = false)
    private int productId;

    @ManyToOne
    @JoinColumn(name = "KEYWORD_ID", referencedColumnName = "KEYWORD_ID", nullable = false, insertable = false, updatable = false)
    private Keyword keywordByKeywordId;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "PRODUCT_ID", nullable = false, insertable = false, updatable = false)
    private Product productByProductId;
}
