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
public class Subcategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUBCATEGORY_ID", nullable = false)
    private int subcategoryId;

    @Basic
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

//    @Basic
//    @Column(name = "CATEGORY_ID", nullable = false)
//    private int categoryId;

    @OneToMany(mappedBy = "subcategoryBySubcategoryId")
    private List<Product> productsBySubcategoryId;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID", nullable = false)
    private Category categoryByCategoryId;
}
