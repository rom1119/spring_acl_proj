package com.example.demo.acl.config;

import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @Autowired
    public CustomPermissionEvaluator(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);

    }



    @Override
    public boolean hasPermission(Authentication authentication, Object o, Object permission) {

        if (permission.equals("OWNER")) {
            return checkUser(authentication, o);
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String entity, Object permission) {

        if (permission.equals("OWNER") ) {
            if (entity.toLowerCase().equals("user")) {
                User user = userRepository.findById((Long) serializable).get();

                if (user == null) {
                    return true;
                }
                CustomUserDetails customUserDetails = new CustomUserDetails(user);

                return customUserDetails.equals(authentication.getPrincipal());
            }

            return false;
        }


        return false;
    }

    private boolean checkUser(Authentication authentication, Object o)
    {
        User user;
        if (o instanceof User) {
            user = (User) o;
        } else if (o instanceof UserDto) {
            UserDto userDto = (UserDto) o;
            user = modelMapper.map(userDto, User.class);
        } else {
            return false;
        }

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        return customUserDetails.equals(authentication.getPrincipal());
    }
}
