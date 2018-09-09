package com.example.demo.acl.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acl_class")
public class AclClass {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

//    @Index(name = "uk_acl_class", columnList = "class")
    @Column(unique = true, name = "class")
    private String classField;

    @Column(unique = true, name = "name")
    private String name;

    @OneToMany(mappedBy = "aclClass")
    private Set<AclObjectIdentity> objectIdentities;

    public AclClass() {
        setObjectIdentities(new HashSet<>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassField() {
        return classField;
    }

    public void setClassField(String classField) {
        this.classField = classField;
    }

    public Set<AclObjectIdentity> getObjectIdentities() {
        return objectIdentities;
    }

    public void setObjectIdentities(Set<AclObjectIdentity> objectIdentities) {
        this.objectIdentities = objectIdentities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
