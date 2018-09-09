package com.example.demo.acl.config;

import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomPermissionFactory extends DefaultPermissionFactory {

    public CustomPermissionFactory() {
        super();
        registerPublicPermissions(CustomPermission.class);
    }
}
