package com.example.ebuydb.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;


@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductKeywordPK implements Serializable {
    @Column(name = "KEYWORD_ID", nullable = false)
    @Id
    private int keywordId;

    @Column(name = "PRODUCT_ID", nullable = false)
    @Id
    private int productId;
}
