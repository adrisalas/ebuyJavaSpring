package com.example.ebuydb.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AccountDTO {
    private int userId;
    private String nickname;
    private String email;
    private String password;
    private short isadmin;
}
