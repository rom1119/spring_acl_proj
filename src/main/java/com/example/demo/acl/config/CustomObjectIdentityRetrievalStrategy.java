package com.example.demo.acl.config;

import com.example.demo.user.model.ResourceInterface;
import com.example.demo.user.model.UserDto;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

public class CustomObjectIdentityRetrievalStrategy implements ObjectIdentityRetrievalStrategy {
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        if (UserDto.class.equals(domainObject.getClass())) {
            return new ObjectIdentityImpl(UserDto.class, ((UserDto)domainObject).getId());
        }

        return new ObjectIdentityImpl(domainObject.getClass(), ((ResourceInterface)domainObject).getId());
        // similar for other domain classes
    }
}