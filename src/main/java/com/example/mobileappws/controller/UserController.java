package com.example.mobileappws.controller;

import com.example.mobileappws.exceptions.UserServiceException;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.shared.dto.UserDto;
import com.example.mobileappws.ui.model.request.UserDetailsRequestModel;
import com.example.mobileappws.ui.model.response.*;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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

    @PutMapping( path = "/{id}",  consumes = {MediaType.APPLICATION_XML_VALUE ,
                    MediaType.APPLICATION_JSON_VALUE},
                     produces = { MediaType.APPLICATION_XML_VALUE ,
                    MediaType.APPLICATION_JSON_VALUE })
    public UserRest updateUser(
                @RequestBody UserDetailsRequestModel userDetails
                , @PathVariable String id){
        UserRest updatedUser = new UserRest();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto updateUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updateUser, updatedUser);

        return updatedUser;
    }

    @DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE ,
            MediaType.APPLICATION_JSON_VALUE })
    public OperationStatusModel deleteUser(@PathVariable String id){

        OperationStatusModel operationStatus = new OperationStatusModel();
        operationStatus.setOperationName(RequestOperationName.DELETE.name());
        userService.deleteUser(id);
        operationStatus.setOpreationResult(RequestOperationStatus.SUCCESS.name());
        return operationStatus;
    }

    @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE ,
                            MediaType.APPLICATION_XML_VALUE})
    public List<UserRest> getUsers(@RequestParam (value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "25") int limit) {

        List<UserRest> returnValue = new ArrayList<>();

        if(page > 0) page = page - 1;

        List<UserDto> users = userService.getUsers(page, limit);

        for (UserDto userDto: users) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(userDto, userModel);
            returnValue.add(userModel);
        }

        return returnValue;
    }

}
