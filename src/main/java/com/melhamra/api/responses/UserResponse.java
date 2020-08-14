package com.melhamra.api.responses;

import lombok.Data;

@Data
public class UserResponse {

    private String userID;
    private String firstName;
    private String lastName;
    private String email;

}
