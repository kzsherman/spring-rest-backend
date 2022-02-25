package com.example.mobileappws.service.impl;

import com.example.mobileappws.UserRepository;
import com.example.mobileappws.io.entity.UserEntity;
import com.example.mobileappws.service.UserService;
import com.example.mobileappws.shared.Utils;
import com.example.mobileappws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        //checking if user email exists in the database
        UserEntity emailExists = userRepository.findByEmail(userDto.getEmail());
        if(emailExists != null) throw new RuntimeException("Record already exists");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);

        String publicUserId = utils.generateUserId(30);
        userEntity.setUserId(publicUserId);

        userEntity.setEmailVerificationToken("toker");
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));

        UserEntity storedUserDetails = userRepository.save(userEntity);

        UserDto returnUser = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnUser);

        return returnUser;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail()
                        , userEntity.getEncryptedPassword()
                        , new ArrayList<>());
    }
}
