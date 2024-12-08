package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {
    List<Book> getAllBooks();
    Optional<Book> getBook(Long id);

    void saveBook(Book book);
    void deleteBook(Long id);
}
