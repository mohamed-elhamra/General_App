package com.melhamra.api.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserRequest {

    @NotNull(message = "First name should not be null")
    @Size(min = 3, max = 50, message = "Size should be between 3 and 50 character")
    private String firstName;
    @NotNull(message = "Last name should not be null")
    @Size(min = 3, max = 50, message = "Size should be between 3 and 50 character")
    private String lastName;
    @NotNull(message = "Email should not be null")
    @Email(message = "Email format is incorrect")
    private String email;
    @NotNull(message = "Password name should not be null")
    @Size(min = 8, max = 12, message = "Size should be between 8 and 12 character")
    private String password;

}

