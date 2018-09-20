package com.example.demo.main.service;

import com.example.demo.main.model.Book;
import com.example.demo.main.repository.BookRepository;
import com.example.demo.user.model.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Book findByIdToAdministration(Long id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public Book findByIdToEdit(Long id) {
        return bookRepository.findById(id).get();
    }

    @Override
    public List<Book> getOneToEdit(Book entity) {
        return getOneMethod(entity);
    }

    @Override
    public List<Book> getOneToAdministration(Book entity) {
        return getOneMethod(entity);
    }

    private List<Book> getOneMethod(Book user)
    {
        Book toEdit = findOne(user.getId());
        List<Book> arr = new ArrayList<>();
        if (toEdit != null) {
            arr.add(toEdit);
        }
        return arr;
    }

    private Book findOne(Long id) {
        return bookRepository.findById(id).get();
    }

}
