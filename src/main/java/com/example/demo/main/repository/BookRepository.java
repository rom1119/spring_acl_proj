package com.example.demo.main.repository;

import com.example.demo.main.model.Book;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Override
    Page<Book> findAll(Pageable pageable);

    @Override
    <S extends Book> Page<S> findAll(Example<S> example, Pageable pageable);

    @Override
    List<Book> findAll(Sort sort);

    @Query("SELECT b FROM Book b WHERE " +
            "LOWER(b.name) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(b.author) LIKE LOWER(CONCAT('%',:searchTerm, '%')) OR " +
            "LOWER(b.description) LIKE LOWER(CONCAT('%',:searchTerm, '%'))")
    Page<Book> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
}
