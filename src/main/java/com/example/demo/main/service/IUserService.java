package com.example.demo.main.service;

import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException;

    User updateUser(UserDto accountDto) throws Exception;
}
