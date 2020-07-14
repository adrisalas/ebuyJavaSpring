package com.example.ebuydb.dto;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Column;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AccountLoginDTO {
    private String emailOrNickname;
    private String password;
}
