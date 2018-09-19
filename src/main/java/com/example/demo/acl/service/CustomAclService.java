package com.example.demo.acl.service;

import com.example.demo.acl.config.AclConfig;
import com.example.demo.acl.config.CustomUserDetails;
import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclSecurityID;
import com.example.demo.acl.repository.AclEntryRepository;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.main.model.ResourceInterface;
import com.example.demo.user.model.AuthorityInterface;
import com.example.demo.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.model.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Component
@Order(1)
public class CustomAclService {

    private MutableAclService aclService;

    private AclSecurityIDRepository securityIDRepository;

    private AclObjectIdentityRepository objectIdentityRepository;

    private AclEntryRepository aclEntryRepository;

    private AclObjectDomainService aclObjectDomainService;

    @Autowired
    public CustomAclService(MutableAclService aclService, AclSecurityIDRepository securityIDRepository, AclObjectIdentityRepository objectIdentityRepository, AclEntryRepository aclEntryRepository, AclObjectDomainService aclObjectDomainService) {
        this.aclService = aclService;
        this.securityIDRepository = securityIDRepository;
        this.objectIdentityRepository = objectIdentityRepository;
        this.aclEntryRepository = aclEntryRepository;
        this.aclObjectDomainService = aclObjectDomainService;
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

    public MutableAcl getAcl(Class c, Serializable id)
    {
        ObjectIdentity oi = new ObjectIdentityImpl(c, id);

// Create or update the relevant ACL
        MutableAcl acl = null;
        try {
            acl = (MutableAcl) aclService.readAclById(oi);
        } catch (NotFoundException nfe) {
            acl = aclService.createAcl(oi);
        }

        aclService.updateAcl(acl);

        return acl;
    }

    public MutableAcl createAclWithUserSid(Class c, Serializable id, User user)
    {
        MutableAcl acl = getAcl(c, id);
        PrincipalSid sid = createUserSid(user);
        acl.setOwner(sid);

        aclService.updateAcl(acl);

        return acl;
    }

    public MutableAcl createAclWithAuthoritySid(Class c, Serializable id, AuthorityInterface authority)
    {
        MutableAcl acl = getAcl(c, id);
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
                }
            }
            permisionClass = permisionClass.getSuperclass();
        }

        return permissionList;
    }

    private PrincipalSid createUserSid(User user)
    {
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return getUserSid(customUserDetails.getUsername());
    }

    private GrantedAuthoritySid createAuthoritySid(AuthorityInterface authority)
    {
        return getAuthoritySid(authority.getName());

    }

    private PrincipalSid getUserSid(String name)
    {
        return new PrincipalSid(name);
    }

    private GrantedAuthoritySid getAuthoritySid(String name)
    {
        return new GrantedAuthoritySid(name);
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

    private Sid getSidFromName(String name)
    {
        AclSecurityID findSid = securityIDRepository.findBySid(name);

        if (findSid.isPrincipal()) {
            return getUserSid(name);
        } else {
            return getAuthoritySid(name);
        }
    }

    public Permission getPermissionFromMask(int mask) throws IllegalAccessException {
        for (Map.Entry<String, Permission> el : getAvailablePermission().entrySet()) {
            if (el.getValue().getMask() == mask) {
                return el.getValue();
            }
        }

        return null;
    }

    public String getPermissionName(Permission permission) throws IllegalAccessException {
        for (Map.Entry<String, Permission> el : getAvailablePermission().entrySet()) {
            if (el.getValue() == permission) {
                return el.getKey();
            }
        }

        return null;
    }


    public AccessControlEntry createAclEntry(AclEntry entity, Class<?> aClass, Long id) throws IllegalAccessException {
        MutableAcl acl = getAcl(aClass, id);

        Sid sid = getSidFromName(entity.getSecurityID().getSid());
//        entity.
        Permission permission = getPermissionFromMask(entity.getMask());

// Now grant some permissions via an access control entry (ACE)
        acl.insertAce(acl.getEntries().size(), permission, sid, entity.isGranting());
        AccessControlEntry accessControlEntry = acl.getEntries().get(acl.getEntries().size() - 1);
//        acl.updateAu(, );
        aclService.updateAcl(acl);

        return accessControlEntry;
    }

    public AccessControlEntry updateAclEntry(AclEntry entity,int indexAce, ResourceInterface objDomain) throws IllegalAccessException {

        MutableAcl acl = getAcl(objDomain.getClass(), objDomain.getId());
        AccessControlEntry accessControlEntry = acl.getEntries().get(indexAce);
        acl.updateAce(indexAce, getPermissionFromMask(entity.getMask()));
        aclService.updateAcl(acl);

        return accessControlEntry;
    }

    public AccessControlEntry deleteAclEntry(int indexAce, ResourceInterface objDomain)
    {
        MutableAcl acl = getAcl(objDomain.getClass(), objDomain.getId());
        AccessControlEntry accessControlEntry = acl.getEntries().get(indexAce);

        acl.deleteAce(indexAce);

        aclService.updateAcl(acl);

        return accessControlEntry;
    }

    public String getSidName(AccessControlEntry accessControlEntry) {
        if (isUserSid(accessControlEntry)) {
            return ((PrincipalSid) accessControlEntry.getSid()).getPrincipal();
        }

        return ((GrantedAuthoritySid) accessControlEntry.getSid()).getGrantedAuthority();

    }

    public boolean isUserSid(AccessControlEntry accessControlEntry)
    {
        if (accessControlEntry.getSid() instanceof PrincipalSid) {
            return true;
        }

        return false;
    }
}
