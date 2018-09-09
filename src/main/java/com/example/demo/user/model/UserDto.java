package com.example.demo.user.model;

import com.example.demo.main.validation.FieldMatch;
import com.example.demo.main.validation.Image;
import com.example.demo.main.validation.group.Registration;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match", groups = {Registration.class})
public class UserDto implements Serializable {

    private String id;

    private Long version;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String password;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String confirmPassword;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String email;
//    @Email

    @Image(maxHeight = 1000, maxWidth = 1000)
    private MultipartFile file;

//    private String fileName;

    @NotNull
    private boolean enabled;

    private UserDetails userDetails;

    private Set<Role> roles;

    public UserDto() {
        setRoles(new HashSet<Role>());
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }



}
