package com.melhamra.api.services;

import com.melhamra.api.dtos.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    UserDto createUser(UserDto userDto);

    UserDto getUserByEmail(String email);

    UserDto getUserByUserId(String id);

    UserDto updateUser(String id, UserDto userDto);

    void deleteUser(String id);

    List<UserDto> getUsers(int page, int limit, String search);
}
