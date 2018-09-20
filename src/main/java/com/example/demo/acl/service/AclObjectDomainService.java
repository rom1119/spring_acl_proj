package com.example.demo.acl.service;

import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.model.AclResourceInterface;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.main.model.ResourceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AclObjectDomainService implements IAclObjectDomainService {

    @Autowired
    private AclObjectIdentityRepository objectIdentityRepository;

    @Override
    public AclResourceInterface checkAccessObjectDomain(AclResourceInterface obj) {
        return obj;
    }
}
