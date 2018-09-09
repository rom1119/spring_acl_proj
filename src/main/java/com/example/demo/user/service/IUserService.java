package com.example.demo.user.service;

import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException;

    User updateUser(UserDto accountDto) throws Exception;

    @PostFilter("hasPermission(filterObject, 'READ')")
    List<User> findAll();

    @PostAuthorize("hasPermission(returnObject, 'WRITE')")
    public User findByIdToEdit(String id);

    @PostAuthorize("hasPermission(returnObject, 'DELETE')")
    public User findByIdToDelete(String id);

    @PreAuthorize("hasPermission(#entity, 'DELETE')")
    public boolean canDelete(@Param("entity") User user);

    @PreAuthorize("hasPermission(#entity, 'WRITE') or #entity.equal()")
    public boolean canEdit(@Param("entity") User user);

}
