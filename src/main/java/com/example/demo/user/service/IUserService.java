package com.example.demo.user.service;

import com.example.demo.acl.service.IAclService;
import com.example.demo.user.exception.EmailExistsException;
import com.example.demo.user.model.CustomUserDetails;
import com.example.demo.user.model.User;
import com.example.demo.user.model.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface IUserService extends IAclService<User> {

    User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException;

    User changePassword(UserDto accountDto);

    User updateUser(UserDto accountDto) throws Exception;

    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('SUPER_ADMIN') or isOwner(returnObject)")
    User[] filterAccessibleElements(Page<User> els, Pageable pageable);

    Page<User> findAll(CustomUserDetails user, Pageable pageable);

    Page<User> findBySearchTerm(CustomUserDetails user, String term, Pageable pageable);

    List<User> findToPage(String term, Pageable pageable);

    @PostAuthorize("hasPermission(returnObject, 'READ') or hasRole('SUPER_ADMIN') or isOwner(returnObject)")
    User findByIdToView(Long id);

    @PostAuthorize("hasPermission(returnObject, 'WRITE') or isOwner(returnObject) ")
    public User findByIdToEdit(Long id);

    @PostAuthorize("hasPermission(returnObject, 'DELETE') or isOwner(returnObject)")
    public User findByIdToDelete(Long id);

    @PostAuthorize("isOwner(returnObject) ")
    User findByIdToChangePassword(Long id);

    @Override
    @PostAuthorize("hasPermission(returnObject, 'ADMINISTRATION') or isOwner(returnObject)")
    User findByIdToAdministration(Long id);

    boolean canDelete(@Param("entity") User user);

    boolean canEdit(@Param("entity") User user);

    boolean canChangePassword(@Param("entity") User user);

    @PostFilter("hasPermission(filterObject, 'WRITE') or isOwner(filterObject)")
    public List<User> getOneToEdit(@Param("entity") User user);

    @PostFilter("hasPermission(filterObject, 'DELETE') or isOwner(filterObject)")
    public List<User> getOneToDelete(@Param("entity") User user);

    @PostFilter("isOwner(filterObject)")
    public List<User> getOneToChangePassword(@Param("entity") User user);

    @PostFilter("hasPermission(filterObject, 'ADMINISTRATION') or isOwner(filterObject)")
    public List<User> getOneToAdministration(@Param("entity") User user);
}
