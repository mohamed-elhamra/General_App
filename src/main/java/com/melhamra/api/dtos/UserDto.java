package com.melhamra.api.dtos;

import lombok.Data;


@Data
public class UserDto {

    private long id;
    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;

}
