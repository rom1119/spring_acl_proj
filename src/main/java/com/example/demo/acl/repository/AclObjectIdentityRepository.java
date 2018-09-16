package com.example.demo.acl.repository;

import com.example.demo.acl.model.AclObjectIdentity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AclObjectIdentityRepository extends JpaRepository<AclObjectIdentity, Long> {

    @Query(
    "SELECT aoi from AclObjectIdentity aoi JOIN aoi.aclClass ac WHERE aoi.objectId = :objectId AND ac.classField = :className "
    )
    AclObjectIdentity findByObjectIdAndClassName(@Param("objectId") String objectId, @Param("className") String className);
}
