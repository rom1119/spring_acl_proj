package com.example.demo.acl.fixtures;

import com.example.demo.acl.config.CustomUserDetails;
import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.repository.AclEntryRepository;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.acl.repository.AclSecurityIDRepository;
import com.example.demo.acl.service.CustomAclService;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.acls.domain.AbstractPermission;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;


import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.Validator;

@Component
@DependsOn({"initialUserData", "initialBookData", "initialAclSecurityIDData", "initialAclObjectIdentityData", "customAclService"})
public class InitialAclEntryData {

    private UserRepository userRepository;

    private CustomAclService customAclService;

    private AclSecurityIDRepository securityIDRepository;

    private AclObjectIdentityRepository objectIdentityRepository;

    private AclEntryRepository aclEntryRepository;

    private Validator validator;

    @Autowired
    public InitialAclEntryData(UserRepository userRepository, CustomAclService customAclService, AclSecurityIDRepository securityIDRepository, AclObjectIdentityRepository objectIdentityRepository, AclEntryRepository aclEntryRepository, Validator validator) {
        this.userRepository = userRepository;
        this.customAclService = customAclService;
        this.securityIDRepository = securityIDRepository;
        this.objectIdentityRepository = objectIdentityRepository;
        this.aclEntryRepository = aclEntryRepository;
        this.validator = validator;
    }

    @PostConstruct
    @Transactional
    public void init() throws IllegalAccessException {
        User userToPermission = userRepository.findById(Long.valueOf(3)).get();
        User objectIdentity = userRepository.findById(Long.valueOf(1)).get();
        AclEntry aclEntry = createAclEntry(BasePermission.READ, userToPermission, objectIdentity);

//        DataBinder binder = new DataBinder(aclEntry);
//        binder.validate();

        aclEntryRepository.save(aclEntry);

    }

    private AclEntry createAclEntry(Permission permission, User userToPermission, User objectIdentity)
    {
        AclEntry aclEntry = new AclEntry();
        aclEntry.setAceOrder(1);

        aclEntry.setAuditFailure(false);
        aclEntry.setAuditSuccess(true);
        aclEntry.setGranting(true);

        aclEntry.setMask(permission.getMask());

       UserDetails customUserDetails = new CustomUserDetails(userToPermission);
        aclEntry.setSecurityID(securityIDRepository.findBySid(customUserDetails.getUsername()));

        AclObjectIdentity aclObjectIdentity = objectIdentityRepository.findByObjectIdAndClassName(String.valueOf(objectIdentity.getId()), objectIdentity.getClass().getName());
        aclEntry.setObjectIdentity(aclObjectIdentity);

        return aclEntry;
    }

}
