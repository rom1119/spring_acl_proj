package com.example.demo.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Component
@Order(1)
public class CustomAclService {

    private MutableAclService aclService;

    @Autowired
    public CustomAclService(MutableAclService aclService) {
        this.aclService = aclService;
    }

    public void createAcl(Class c, Serializable id)
    {
        ObjectIdentity oi = new ObjectIdentityImpl(c, id);
//            Sid sid = new PrincipalSid("sadm");
//        Permission p = BasePermission.ADMINISTRATION;

// Create or update the relevant ACL
        MutableAcl acl = null;
        try {
            acl = (MutableAcl) aclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oi);
        }

// Now grant some permissions via an access control entry (ACE)
//            acl.insertAce(acl.getEntries().size(), p, sid, true);
        aclService.updateAcl(acl);
    }
}
