package com.melhamra.api.controllers;

import com.melhamra.api.dtos.AddressDto;
import com.melhamra.api.requests.AddressRequest;
import com.melhamra.api.responses.AddressResponse;
import com.melhamra.api.services.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<AddressResponse>> getAddresses(Principal principal){
        List<AddressResponse> addressResponses = addressService.getAllAddresses(principal.getName()).stream()
                                                .map(address -> new ModelMapper().map(address, AddressResponse.class))
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(addressResponses);
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<AddressResponse> saveAddress(@RequestBody AddressRequest addressRequest,
                                                       Principal principal){
        ModelMapper modelMapper = new ModelMapper();
        AddressDto addressDto = modelMapper.map(addressRequest, AddressDto.class);
        AddressDto createdAddress = addressService.createAddress(addressDto, principal.getName());
        AddressResponse newAddress = modelMapper.map(createdAddress, AddressResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddressResponse> getAddress(@PathVariable String id){
        AddressDto addressDto = addressService.getAddress(id);
        AddressResponse addressResponse = new ModelMapper().map(addressDto, AddressResponse.class);
        return ResponseEntity.ok(addressResponse);
    }

    @PutMapping(
            value = "/{id}",
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<AddressResponse> updateAddress(@RequestBody AddressRequest addressRequest,
                                                         @PathVariable String id){
        ModelMapper modelMapper = new ModelMapper();
        AddressDto addressDto = modelMapper.map(addressRequest, AddressDto.class);
        AddressDto updatedAddress = addressService.updateAddress(addressDto, id);
        AddressResponse newAddress = modelMapper.map(updatedAddress, AddressResponse.class);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(newAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable String id){
        addressService.deleteAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
