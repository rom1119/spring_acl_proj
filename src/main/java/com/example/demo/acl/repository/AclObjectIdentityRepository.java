package com.example.demo.acl.repository;

import com.example.demo.acl.model.AclObjectIdentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {
}
