package com.melhamra.api.responses;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean admin;
    private List<AddressResponse> addresses;
    private ContactResponse contact;

}
