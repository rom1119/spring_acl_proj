package com.example.demo.user.model;

import com.example.demo.main.validation.FieldMatch;
import com.example.demo.main.validation.ValidChangePassword;
import com.example.demo.main.validation.group.PasswordChange;
import com.example.demo.main.validation.group.Registration;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match", groups = {Registration.class, PasswordChange.class})
@ValidChangePassword(oldPasswordField = "oldPassword", passwordField = "password", confirmPasswordField = "confirmPassword", groups = {PasswordChange.class})
public class UserDto implements Serializable {

    private Long id;

    private Long version;

    @NotNull(groups = {PasswordChange.class})
    @NotEmpty(groups = {PasswordChange.class})
    private String oldPassword;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String password;

    @NotNull(groups = {Registration.class, PasswordChange.class})
    @NotEmpty(groups = {Registration.class, PasswordChange.class})
    private String confirmPassword;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String email;
//    @Email

//    private String fileName;

    @NotNull
    private boolean enabled;

    private UserDetails userDetails;

    private Set<Role> roles;

    public UserDto() {
        setRoles(new HashSet<Role>());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

}
