package com.melhamra.api.dtos;

import lombok.Data;

@Data
public class AddressDto {

    private long id;
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String postal;
    private String type;
    private UserDto user;

}
