package com.melhamra.api.controllers;

import com.melhamra.api.dtos.UserDto;
import com.melhamra.api.exceptions.UserException;
import com.melhamra.api.requests.UserRequest;
import com.melhamra.api.responses.UserResponse;
import com.melhamra.api.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
            )
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest){

        if(userRequest.getFirstName().isEmpty()) throw new UserException("First name is required");

        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userRequest, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);

        UserResponse userResponse = modelMapper.map(createdUser, UserResponse.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                                                          @RequestParam(value = "limit", defaultValue = "2") int limit,
                                                          @RequestParam(value = "search", defaultValue = "") String search){
        ModelMapper modelMapper = new ModelMapper();
        List<UserDto> users = userService.getUsers(page, limit, search);

        List<UserResponse> userResponses =
                users.stream().map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userResponses);
    }


    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<UserResponse> getUser(@PathVariable String id){
        UserDto userDto = userService.getUserByUserId(id);
        UserResponse userResponse = new ModelMapper().map(userDto, UserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @PutMapping(
            path = "/{id}",
            consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE}
            )
    public ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userRequest){
        UserDto userDto = new ModelMapper().map(userRequest, UserDto.class);

        UserDto updatedUser = userService.updateUser(id ,userDto);

        UserResponse userResponse = new ModelMapper().map(updatedUser, UserResponse.class);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("user was deleted successfully");
    }

}
