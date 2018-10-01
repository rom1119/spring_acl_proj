package com.example.demo.user.model;

import com.example.demo.main.model.ResourceInterface;
import com.example.demo.main.validation.Image;
import com.example.demo.main.validation.group.Registration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table( name = "user_details" )
public class UserDetails implements ResourceInterface, FileInterface {

    @Id
//    @GeneratedValue(generator = "uuid")
//    @GenericGenerator(name = "uuid", strategy = "uuid")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String firstName;

    @NotNull(groups = {Registration.class})
    @NotEmpty(groups = {Registration.class})
    private String lastName;

    @Column(name = "file_name")
    private String fileName;

    @Transient
    @Image(maxHeight = 1000, maxWidth = 1000)
    private MultipartFile file;

    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private int score;

    public UserDetails() {
    }

    public UserDetails(String firstName,
                       String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @JsonIgnore
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public MultipartFile getFile() {
        return file;
    }

    @Override
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
