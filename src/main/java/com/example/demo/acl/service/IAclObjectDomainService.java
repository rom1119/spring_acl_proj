package com.example.demo.acl.service;

import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.main.model.ResourceInterface;
import org.springframework.security.access.prepost.PostAuthorize;

public interface IAclObjectDomainService {

    @PostAuthorize("hasPermission(#obj, 'ADMINISTRATION') or isOwner(returnObject)")
    AclObjectIdentity checkAccessObjectDomain(ResourceInterface obj);
}
