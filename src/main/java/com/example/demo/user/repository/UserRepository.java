package com.example.demo.user.repository;

import com.example.demo.user.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findById(Long id);

    public User findByEmail(String email);

    @Query(
            "SELECT DISTINCT u FROM User u " +
            "WHERE " +
            "u.id IN " +
            "(" +
                "SELECT DISTINCT oI.objectId from AclObjectIdentity oI" +
                " JOIN oI.aclClass aclClass " +
                " JOIN oI.owner owner " +
                " WHERE " +
                " aclClass.classField = :objectClass " +
                " AND " +
                " owner.sid = :userToCheck " +
                " AND " +
                " owner.principal = true " +
            ")" +
            " OR " +
            "u.id IN " +
            " ( " +
                "SELECT DISTINCT oI.objectId from AclObjectIdentity oI" +
                " JOIN oI.aclClass aclClass " +
                " LEFT JOIN oI.entries entry " +
                " LEFT JOIN entry.securityID s " +
                " WHERE " +
                " aclClass.classField = :objectClass " +
                " AND " +
                " s.sid = :userToCheck" +
                " AND " +
                " s.principal = true " +
                " AND " +
                " entry.granting = true " +
                " AND " +
                " entry.mask = :mask" +
            " ) "
    )
    Page<User> findAllCustomPageable(
            @Param("objectClass") String objectClass,
            @Param("userToCheck") String userToCheck,
            @Param("mask") int mask,
            Pageable pageable);

    @Query(
            "SELECT DISTINCT u FROM User u " +
            "JOIN u.roles r " +
            "JOIN u.userDetails ud " +
            "WHERE " +
            " u.id IN " +
                " ( " +
                "SELECT DISTINCT oI.objectId from AclObjectIdentity oI" +
                " JOIN oI.aclClass aclClass " +
                " LEFT JOIN oI.entries entry " +
                " LEFT JOIN entry.securityID s " +
                " WHERE " +
                " aclClass.classField = :objectClass " +
                " AND " +
                " s.sid = :userToCheck" +
                " AND " +
                " s.principal = true " +
                " AND " +
                " entry.granting = true " +
                " AND " +
                " entry.mask = :mask" +
                " ) " +
            " AND " +
                " (" +
                "LOWER(u.email) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
                "LOWER(r.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
                "LOWER(ud.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
                "LOWER(ud.lastName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
                " ) " +

            " OR " +
            "u.id IN " +
                "(" +
                "SELECT DISTINCT oI.objectId from AclObjectIdentity oI" +
                " JOIN oI.aclClass aclClass " +
                " JOIN oI.owner owner " +
                " WHERE " +
                " aclClass.classField = :objectClass " +
                " AND " +
                " owner.sid = :userToCheck " +
                " AND " +
                " owner.principal = true " +
                ")" +

            " AND " +
            " (" +
            "LOWER(u.email) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(r.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(ud.firstName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(ud.lastName) LIKE LOWER(CONCAT('%',:searchTerm, '%')) " +
            " ) "

    )
    Page<User> findBySearchTermPageable(
            @Param("objectClass") String objectClass,
            @Param("userToCheck") String userToCheck,
            @Param("mask") int mask,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

}
