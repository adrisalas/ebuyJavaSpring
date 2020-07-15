package com.example.ebuydb.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AccountSessionDTO {
    private int userId;
    private String nickname;
    private String email;
    private short isadmin;
}