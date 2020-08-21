package com.melhamra.api.services;

import com.melhamra.api.dtos.UserDto;
import com.melhamra.api.entities.AddressEntity;
import com.melhamra.api.entities.UserEntity;
import com.melhamra.api.repositories.UserRepository;
import com.melhamra.api.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Utils utils;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity checkUser = userRepository.findByEmail(userDto.getEmail());

        if(checkUser != null) throw new RuntimeException("User already exists!");

        ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userEntity.getContact().setContactId(utils.generateStringId(32));
        userEntity.getContact().setUser(userEntity);
        for (int i = 0; i < userEntity.getAddresses().size(); i++) {
            AddressEntity addressEntity = userEntity.getAddresses().get(i);
            addressEntity.setAddressId(utils.generateStringId(32));
            addressEntity.setUser(userEntity);
            userEntity.getAddresses().set(i, addressEntity);
        }
        userEntity.setUserID(utils.generateStringId(32));
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity newUser = userRepository.save(userEntity);

        UserDto userDto1 = modelMapper.map(newUser, UserDto.class);

        return userDto1;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null ) throw new UsernameNotFoundException(email);
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity,userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserID(userId);
        if(userEntity == null) throw new UsernameNotFoundException(userId);
        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserEntity userEntity = userRepository.findByUserID(id);
        if(userEntity == null) throw new UsernameNotFoundException(id);

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedUserEntity = userRepository.save(userEntity);
        UserDto updatedUserDto = new ModelMapper().map(updatedUserEntity, UserDto.class);
        return updatedUserDto;
    }

    @Override
    public void deleteUser(String id) {
        UserDto userDto = getUserByUserId(id);
        UserEntity userEntity = new ModelMapper().map(userDto, UserEntity.class);
        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit, String search) {

        if(page > 0) page -= 1;

        ModelMapper modelMapper = new ModelMapper();
        PageRequest pageable = PageRequest.of(page, limit);
        Page<UserEntity> userEntityPage;

        if (search.isEmpty() || search.isBlank()){
            userEntityPage = userRepository.findAllUsers(pageable);
        }else {
            userEntityPage = userRepository.findAllUsersByCriteria(pageable, search);
        }

        List<UserEntity> userEntityList = userEntityPage.getContent();

        List<UserDto> userDtoList = userEntityList.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        return userDtoList;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email);
        if(userEntity == null ) throw new UsernameNotFoundException(email);
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
