package com.example.demo.acl.service;

import com.example.demo.acl.model.AclResourceInterface;

public interface IAclService<T extends AclResourceInterface> {
    T findByIdToAdministration(Long id);
}
