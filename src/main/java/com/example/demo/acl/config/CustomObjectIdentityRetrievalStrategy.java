package com.example.demo.acl.config;

import com.example.demo.main.model.ResourceInterface;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;

public class CustomObjectIdentityRetrievalStrategy implements ObjectIdentityRetrievalStrategy {
    public ObjectIdentity getObjectIdentity(Object domainObject) {
        if (!(domainObject instanceof ResourceInterface)) {
            return null;
        }

        return new ObjectIdentityImpl(domainObject.getClass(), ((ResourceInterface)domainObject).getId());
        // similar for other domain classes
    }
}