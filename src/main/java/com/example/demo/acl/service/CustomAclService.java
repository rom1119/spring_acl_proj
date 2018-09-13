package com.example.demo.acl.service;

import com.example.demo.acl.config.AclConfig;
import com.example.demo.acl.config.CustomPermission;
import com.example.demo.acl.config.CustomUserDetails;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.user.model.AuthorityInterface;
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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Component
@Order(1)
public class CustomAclService {

    private MutableAclService aclService;

    @Autowired
    public CustomAclService(MutableAclService aclService) {
        this.aclService = aclService;
    }

    public List<AccessControlEntry> getAclEntries(Class c, Serializable id)
    {
        ObjectIdentity oi = new ObjectIdentityImpl(c, id);

// Create or update the relevant ACL
        MutableAcl acl = null;
        try {
            acl = (MutableAcl) aclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oi);
        }

        return acl.getEntries();
    }

    public MutableAcl createAcl(Class c, Serializable id)
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

        return acl;
    }

    public MutableAcl createAclWithUserSid(Class c, Serializable id, User user)
    {
        MutableAcl acl = createAcl(c, id);
        PrincipalSid sid = createUserSid(user);
        acl.setOwner(sid);

        aclService.updateAcl(acl);

        return acl;
    }

    public MutableAcl createAclWithAuthoritySid(Class c, Serializable id, AuthorityInterface authority)
    {
        MutableAcl acl = createAcl(c, id);
        GrantedAuthoritySid sid = createAuthoritySid(authority);
        acl.setOwner(sid);

        aclService.updateAcl(acl);

        return acl;
    }

    public Map<String, Permission> getAvailablePermission() throws IllegalAccessException {
        Map<String, Permission> permissionList = new HashMap<>();
        Class permisionClass = AclConfig.permissionClass;

        while(permisionClass.getSuperclass() != null) {
            Field[] fields = permisionClass.getDeclaredFields();
            for (Field f : fields) {
                if (Modifier.isStatic(f.getModifiers())) {
                    permissionList.put(f.getName(), (Permission) f.get((Object) f.getName()));
//                    System.out.println(f.getName());
//                    System.out.println();
                }
            }
            permisionClass = permisionClass.getSuperclass();
        }


        permissionList.forEach((String el, Permission e) -> {
            System.out.println(e.getMask());

        });


        return permissionList;
    }

    private PrincipalSid createUserSid(User user)
    {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        PrincipalSid sid = new PrincipalSid(customUserDetails.getUsername());

        return sid;
    }

    private GrantedAuthoritySid createAuthoritySid(AuthorityInterface authority)
    {
        GrantedAuthoritySid sid = new GrantedAuthoritySid(authority.getName());

        return sid;
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
