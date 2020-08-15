package com.melhamra.api.dtos;

import lombok.Data;

@Data
public class ContactDto {

    private long id;
    private String contactId;
    private String mobile;
    private String skype;
    private UserDto user;

}
