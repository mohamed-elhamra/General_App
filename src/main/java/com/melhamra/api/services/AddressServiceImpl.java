package com.melhamra.api.services;

import com.melhamra.api.dtos.AddressDto;
import com.melhamra.api.entities.AddressEntity;
import com.melhamra.api.entities.UserEntity;
import com.melhamra.api.repositories.AddressRepository;
import com.melhamra.api.repositories.UserRepository;
import com.melhamra.api.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AddressDto> getAllAddresses(String email) {

        UserEntity currentUser = userRepository.findByEmail(email);

        List<AddressEntity> addressEntities =
                currentUser.getAdmin() == true ? addressRepository.findAll() : addressRepository.findAllByUser(currentUser);
        List<AddressDto> addresses = addressEntities.stream()
                                    .map(address -> new ModelMapper().map(address, AddressDto.class))
                                    .collect(Collectors.toList());
        return addresses;
    }

    @Override
    public AddressDto createAddress(AddressDto addressDto, String email) {
        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = userRepository.findByEmail(email);

        AddressEntity addressEntity = modelMapper.map(addressDto, AddressEntity.class);
        addressEntity.setAddressId(new Utils().generateStringId(32));
        addressEntity.setUser(userEntity);

        AddressEntity savedAddress = addressRepository.save(addressEntity);

        AddressDto newAddress = modelMapper.map(savedAddress, AddressDto.class);
        return newAddress;
    }

    @Override
    public AddressDto getAddress(String addressId) {
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        return new ModelMapper().map(addressEntity, AddressDto.class);
    }

    @Override
    public AddressDto updateAddress(AddressDto addressDto, String addressId) {
        ModelMapper modelMapper = new ModelMapper();
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        addressEntity.setCity(addressDto.getCity());
        addressEntity.setCountry(addressDto.getCountry());
        addressEntity.setStreet(addressDto.getStreet());
        addressEntity.setPostal(addressDto.getPostal());
        addressEntity.setType(addressDto.getType());

        AddressEntity updatedAddress = addressRepository.save(addressEntity);

        AddressDto newAddress = modelMapper.map(updatedAddress, AddressDto.class);
        return newAddress;
    }

    @Override
    public void deleteAddress(String addressId) {
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        addressRepository.delete(addressEntity);
    }
}
