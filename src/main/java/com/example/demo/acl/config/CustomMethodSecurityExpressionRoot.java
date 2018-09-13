package com.example.demo.acl.config;

import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserRepository userRepository;

    private ModelMapper modelMapper;
    private Object filterObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isOwner(Long id, String entityType) {
//        System.out.println(id);
//        System.out.println(entityType);
//        System.out.println("DDD");
//        System.out.println(userRepository);
        if (entityType.equalsIgnoreCase("user")) {
            Optional<User> user = userRepository.findById((Long) id);
//        System.out.println(user.get());
                if (user == null) {
                    return true;
                }
                CustomUserDetails customUserDetails = new CustomUserDetails(user.get());

                return customUserDetails.equals(authentication.getPrincipal());
        }


        return false;
    }

    public boolean isOwner(Object object) {

        return checkUser(object);

    }

    private boolean checkUser(Object o)
    {
//        System.out.println(o);
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

    @Override
    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        super.setPermissionEvaluator(permissionEvaluator);
    }

    @Override
    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        super.setTrustResolver(trustResolver);
    }

    @Override
    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        super.setRoleHierarchy(roleHierarchy);
    }

    @Override
    public void setFilterObject(Object o) {
        filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object o) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public Object getThis() {
        return this;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ModelMapper getModelMapper() {
        return modelMapper;
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
