package com.example.mobileappws.controller;

import com.example.mobileappws.exceptions.UserServiceException;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.shared.dto.UserDto;
import com.example.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws.ui.model.response.ErrorMessages;
import com.example.mobileappws.ui.model.response.UserRest;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.awt.*;

@RestController
@RequestMapping("users") //http://localhost:8080/users
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE ,
                                                MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id){
        UserRest returnUser = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, returnUser);
        return returnUser;
    }

    @PostMapping(   consumes = {MediaType.APPLICATION_XML_VALUE ,
                                MediaType.APPLICATION_JSON_VALUE},
                     produces = { MediaType.APPLICATION_XML_VALUE ,
                                MediaType.APPLICATION_JSON_VALUE })
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
        UserRest returnUser = new UserRest();

        if(userDetails.getFirstName().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createUser, returnUser);

        return returnUser;
    }

    @PutMapping
    public String updateUser(){
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser(){
        return "delete user was called";
    }

}
