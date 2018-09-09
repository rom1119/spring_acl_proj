package com.example.demo.acl.service;

import com.example.demo.acl.config.CustomPermission;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public boolean isGranted(ObjectIdentity checkedObject, Authentication authentication, Permission permission)
    {
        Acl acl = aclService.readAclById(checkedObject);
        return acl.isGranted(Arrays.asList(permission), getSidsFromAuthenticationUser(authentication), true);
    }

    private List<Sid> getSidsFromAuthenticationUser(Authentication authentication)
    {
        List<Sid> sids = new ArrayList<>();
        sids.add(new PrincipalSid(authentication));

        authentication.getAuthorities().stream().forEach(el -> {
            sids.add(new GrantedAuthoritySid(el));
        });

        return sids;
    }
}
