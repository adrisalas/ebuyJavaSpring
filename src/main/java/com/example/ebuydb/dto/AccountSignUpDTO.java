package com.example.ebuydb.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AccountSignUpDTO {

    private String nickname;
    private String email1;
    private String email2;
    private String password1;
    private String password2;
}
