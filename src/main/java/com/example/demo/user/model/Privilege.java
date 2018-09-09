package com.example.demo.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "privilege" )
public class Privilege implements ResourceInterface {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles;

    public Privilege() {
        setRoles(new HashSet<Role>());
    }

    public Privilege(String name) {
        this();
        this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public Set<Role> getRoles() {
        return roles;
    }

    public Privilege setRoles(Set<Role> roles) {
        this.roles = roles;

        return this;

    }

    public Privilege addRole(Role role)
    {
        roles.add(role);

        return this;

    }

    public Privilege removeRole(Role role)
    {
        roles.remove(role);

        return this;

    }
}
