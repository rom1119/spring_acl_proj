package com.example.demo.user.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table( name = "role" )
public class Role implements ResourceInterface {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Column
    private String name;

//    @JsonBackReference
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(
            name = "role_has_privilege",
            joinColumns = { @JoinColumn(name = "role_id") },
            inverseJoinColumns = { @JoinColumn(name = "privilege_id") }
    )
    private Set<Privilege> privileges;

    public Role() {
        setUsers(new HashSet<User>());
        setPrivileges(new HashSet<Privilege>());
    }

    public Role(String name) {
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
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Role addUser(User user)
    {
        users.add(user);

        return this;

    }

    public Role removeUser(User user)
    {
        users.remove(user);

        return this;

    }

    public Set<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Role addPrivilege(Privilege privilege)
    {
        privileges.add(privilege);

        return this;

    }

    public Role removePrivilege(Privilege privilege)
    {
        privileges.remove(privilege);

        return this;

    }

    @Override
    public boolean equals(Object o) {
        if (this== o) return true;
        if (o ==null|| getClass() != o.getClass()) return false;

        Role that = (Role) o;

        if (getId() !=null?!getId().equals(that.getId()) : that.getId() !=null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (getId() !=null? getId().hashCode() : 0);
    }
}
