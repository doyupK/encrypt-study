package com.example.encrypt.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMemberDto {
    private String id;
    private String password;
    private String username;
    private String address;
    private String phoneNumber;
}
