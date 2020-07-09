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
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KEYWORD_ID", nullable = false)
    private int keywordId;

    @Basic
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

    @OneToMany(mappedBy = "keywordByKeywordId")
    private List<ProductKeyword> productKeywordsByKeywordId;
}
