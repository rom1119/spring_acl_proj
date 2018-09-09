package com.example.demo.acl.repository;

import com.example.demo.acl.model.AclClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AclClassRepository extends JpaRepository<AclClass, Long> {

    public AclClass findByClassField(String classField);
}
