package com.example.demo.acl.repository;

import com.example.demo.acl.model.AclSecurityID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AclSecurityIDRepository extends JpaRepository<AclSecurityID, Long> {
    AclSecurityID findBySid(String sid);
}
