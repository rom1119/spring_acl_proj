package com.example.demo.acl.service;

import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclEntryDto;
import com.example.demo.acl.model.AclResourceInterface;
import com.example.demo.user.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AclEntryService implements IAclEntryService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MutableAclService aclService;

    @Override
    public AclEntryDto prepareToEdit(AccessControlEntry aclEntry) {
        AclEntryDto aclEntryDto = new AclEntryDto();
        aclEntryDto.setMask(aclEntry.getPermission().getMask());
        return aclEntryDto;
    }

    @Override
    public Optional<AccessControlEntry> getAce(AclResourceInterface user, int aclEntryIndex) {

        ObjectIdentity oi = new ObjectIdentityImpl(user.getClass(), user.getId());
        Acl acl = aclService.readAclById(oi);
        System.out.println(acl.getEntries());
        return Optional.of(acl.getEntries().get(aclEntryIndex));
    }

    @Override
    public AclEntry prepareFromEdit(AclEntryDto entity) {
        AclEntry aclEntry = new AclEntry();
        aclEntry.setMask(entity.getMask());

        return aclEntry;
    }
}
