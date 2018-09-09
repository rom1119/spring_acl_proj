package com.example.demo.acl.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acl_sid")
public class AclSecurityID {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    private boolean principal;

    @Column(unique = true)
    private String sid;

    @OneToMany(mappedBy = "owner")
    private Set<AclObjectIdentity> objectIdentities;

    @OneToMany(mappedBy = "securityID")
    private Set<AclEntry> aclEntries;

    public AclSecurityID() {
        setObjectIdentities(new HashSet<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public Set<AclObjectIdentity> getObjectIdentities() {
        return objectIdentities;
    }

    public void setObjectIdentities(Set<AclObjectIdentity> objectIdentities) {
        this.objectIdentities = objectIdentities;
    }

    public Set<AclEntry> getAclEntries() {
        return aclEntries;
    }

    public void setAclEntries(Set<AclEntry> aclEntries) {
        this.aclEntries = aclEntries;
    }
}
