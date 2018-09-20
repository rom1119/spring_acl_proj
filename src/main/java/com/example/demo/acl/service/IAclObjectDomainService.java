package com.example.demo.acl.service;

import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclResourceInterface;
import com.example.demo.main.model.ResourceInterface;
import org.springframework.security.access.prepost.PostAuthorize;

public interface IAclObjectDomainService {

    @PostAuthorize("hasPermission(returnObject, 'ADMINISTRATION') or isOwner(returnObject)")
    AclResourceInterface checkAccessObjectDomain(AclResourceInterface obj);
}
