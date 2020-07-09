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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID", nullable = false)
    private int categoryId;

    @Basic
    @Column(name = "NAME", nullable = false, length = 30)
    private String name;

    @OneToMany(mappedBy = "categoryByCategoryId")
    private List<Subcategory> subcategoriesByCategoryId;
}
