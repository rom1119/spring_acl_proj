package com.example.demo.acl.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acl_object_identity")
public class AclObjectIdentity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne
    @JoinColumn(name="object_id_class", nullable=false)
    private AclClass aclClass;

    @Column(name = "object_id_identity")
    private String objectId;

    @ManyToOne
    @JoinColumn(name="parent_object", nullable=true)
    private AclObjectIdentity parent;

    @OneToMany(mappedBy = "parent")
    private Set<AclObjectIdentity> children;

    @ManyToOne
    @JoinColumn(name="owner_sid", nullable=false)
    private AclSecurityID owner;

    @Column(name = "entries_inheriting")
    private boolean entriesInheriting;

    @OneToMany(mappedBy = "objectIdentity")
    private Set<AclEntry> entries;

    public AclObjectIdentity() {
        setChildren(new HashSet<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AclClass getAclClass() {
        return aclClass;
    }

    public void setAclClass(AclClass aclClass) {
        this.aclClass = aclClass;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public AclObjectIdentity getParent() {
        return parent;
    }

    public void setParent(AclObjectIdentity parent) {
        this.parent = parent;
    }

    public Set<AclObjectIdentity> getChildren() {
        return children;
    }

    public void setChildren(Set<AclObjectIdentity> children) {
        this.children = children;
    }

    public AclSecurityID getOwner() {
        return owner;
    }

    public void setOwner(AclSecurityID owner) {
        this.owner = owner;
    }

    public boolean isEntriesInheriting() {
        return entriesInheriting;
    }

    public void setEntriesInheriting(boolean entriesInheriting) {
        this.entriesInheriting = entriesInheriting;
    }

    public Set<AclEntry> getEntries() {
        return entries;
    }

    public void setEntries(Set<AclEntry> entries) {
        this.entries = entries;
    }
}
