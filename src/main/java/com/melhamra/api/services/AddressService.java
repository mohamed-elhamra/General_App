package com.melhamra.api.services;

import com.melhamra.api.dtos.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAllAddresses(String email);

    AddressDto createAddress(AddressDto addressDto, String email);

    AddressDto getAddress(String addressId);

    AddressDto updateAddress(AddressDto addressDto, String addressId);

    void deleteAddress(String addressId);
}
