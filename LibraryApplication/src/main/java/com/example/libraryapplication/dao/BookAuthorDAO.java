package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.BookAuthor;

import java.util.List;

public interface BookAuthorDAO {
    List<BookAuthor> findByBookIdAndAuthorId(Long bookId, Long authorId);
    List<BookAuthor> findByBookId(Long bookId);
    List<BookAuthor> findByAuthorId(Long authorId);
    List<BookAuthor> findAll();
    void saveBookAuthor(BookAuthor bookAuthor);
    void deleteBookAuthor(BookAuthor bookAuthor);
}
