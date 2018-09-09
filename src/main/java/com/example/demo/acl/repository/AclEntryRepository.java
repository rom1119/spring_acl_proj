package com.example.demo.acl.repository;

import com.example.demo.acl.model.AclEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AclEntryRepository extends JpaRepository<AclEntry, Long> {
}
