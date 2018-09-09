package com.example.demo.user.repository;

import com.example.demo.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @PreAuthorize("hasPermission(returnObject, 'WRITE')")
    public Optional<User> findById(String id);

    public User findByEmail(String email);

}
