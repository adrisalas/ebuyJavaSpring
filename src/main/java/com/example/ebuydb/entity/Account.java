package com.example.ebuydb.entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private long id;

    @Column(unique = true)
    @NonNull @Size(min = 1, max = 20)
    private String nickname;

    @Column(unique = true)
    @NonNull @Size(min = 1, max = 50)
    private String email;

    @NonNull @Size(min = 1, max = 20)
    private String password;

    @NonNull private short isAdmin;

}

