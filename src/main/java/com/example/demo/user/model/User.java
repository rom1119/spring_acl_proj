package com.example.demo.user.model;

import com.example.demo.user.eventListener.UserEntityListener;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table( name = "user" )
@EntityListeners(UserEntityListener.class)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonDeserialize()
@NamedQuery(name="User.findAllWhereIsAsdInFirstName", query="SELECT u from User u where u.firstName LIKE '%asd%'")
//@JsonIgnoreProperties({"id", "firstName"})
//@JsonPropertyOrder({ "name", "id" })
@Secured("USER_OWNER")
public class User implements FileInterface, ResourceInterface {

    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Version
    private Long version;

    private String firstName;

    private String lastName;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "file_name")
    private String fileName;

    @Transient
    private MultipartFile file;

    private String email;

    private boolean enabled;

    private boolean tokenExpired;


    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY, optional = false, orphanRemoval = true)
    private UserDetails userDetails;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "user_has_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private Set<Role> roles;

    public User() {
        setRoles(new HashSet<Role>());
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @JsonIgnore()
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public User setRoles(Set<Role> roles)
    {
        this.roles = roles;

        return this;

    }

    public User addRole(Role role)
    {
        roles.add(role);

        return this;

    }

    public User removeRole(Role role)
    {
        roles.remove(role);

        return this;

    }

//    @JsonIgnore
    public Set<Role> getRoles()
    {
        return roles;

    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
        userDetails.setUser(this);
    }

    @Override
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public MultipartFile getFile() {
        return file;
    }


}
