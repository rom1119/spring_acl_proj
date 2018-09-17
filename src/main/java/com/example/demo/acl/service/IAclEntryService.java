package com.example.demo.acl.service;

import com.example.demo.acl.model.AclEntry;
import com.example.demo.acl.model.AclEntryDto;
import com.example.demo.user.model.User;
import org.springframework.security.acls.model.AccessControlEntry;

import java.util.Optional;

public interface IAclEntryService {

    AclEntryDto prepareToEdit(AccessControlEntry aclEntry);

    Optional<AccessControlEntry> getAce(User user, int aclEntryIndex);

    AclEntry prepareFromEdit(AclEntryDto entity);
}
