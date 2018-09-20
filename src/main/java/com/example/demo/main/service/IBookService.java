package com.example.demo.main.service;

import com.example.demo.acl.service.IAclService;
import com.example.demo.main.model.Book;
import com.example.demo.user.model.User;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface IBookService extends IAclService<Book> {

    @Override
    @PostAuthorize("hasPermission(returnObject, 'ADMINISTRATION') or isOwner(returnObject)")
    Book findByIdToAdministration(Long id);

    @PostAuthorize("hasPermission(returnObject, 'WRITE') or isOwner(returnObject) ")
    Book findByIdToEdit(Long id);

    @PostFilter("hasPermission(filterObject, 'WRITE') or isOwner(filterObject)")
    public List<Book> getOneToEdit(@Param("entity") Book entity);

    @PostFilter("hasPermission(filterObject, 'ADMINISTRATION') or isOwner(filterObject)")
    public List<Book> getOneToAdministration(@Param("entity") Book entity);

}
