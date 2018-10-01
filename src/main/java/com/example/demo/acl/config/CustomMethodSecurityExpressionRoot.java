package com.example.demo.acl.config;

import com.example.demo.acl.model.AclResourceInterface;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.model.CustomUserDetails;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import com.example.demo.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private CustomAclService aclService;

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isOwner(Long id, String entityType) {
//        System.out.println(id);
//        System.out.println(entityType);
        System.out.println("DDD");
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


        if (object == null) {
            return true;
        }
//        System.out.println(object instanceof User);
//        System.out.println(((User)object).getId() );
//        System.out.println("isNull");

        if (object instanceof User || object instanceof UserDto ) {
            return checkUser(object);

        } else {
            return checkAclObjectIdentity((AclResourceInterface) object);
        }

    }

    private boolean checkAclObjectIdentity(AclResourceInterface object) {
        MutableAcl acl = aclService.getAcl(object.getClass(), object.getId());

        String sidNameOwnerFromAcl = aclService.getSidNameOwnerFromAcl(acl);

        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return  false;
        }

        return ((CustomUserDetails) authentication.getPrincipal()).getUsername().equalsIgnoreCase(sidNameOwnerFromAcl);
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
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getThis() {
        return this.target;
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

    public CustomAclService getAclService() {
        return aclService;
    }

    public void setAclService(CustomAclService aclService) {
        this.aclService = aclService;
    }
}
