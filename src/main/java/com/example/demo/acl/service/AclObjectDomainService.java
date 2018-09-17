package com.example.demo.acl.service;

import com.example.demo.acl.model.AclObjectIdentity;
import com.example.demo.acl.repository.AclObjectIdentityRepository;
import com.example.demo.main.model.ResourceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AclObjectDomainService implements IAclObjectDomainService {

    @Autowired
    private AclObjectIdentityRepository objectIdentityRepository;

    @Override
    public AclObjectIdentity chackAccessObjectDomain(ResourceInterface obj) {
        return objectIdentityRepository.findByObjectIdAndClassName(String.valueOf(obj.getId()), obj.getClass().getName());
    }
}
