package com.example.libraryapplication.repository;

import com.example.libraryapplication.dataModel.Book;
import com.example.libraryapplication.dataModel.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);
    List<Book> findByPublisher(String publisher);
    List<Book> findByISBN(String ISBN);
}
