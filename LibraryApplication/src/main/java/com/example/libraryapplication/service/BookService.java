package com.example.libraryapplication.service;

import com.example.libraryapplication.dataModel.Book;
import com.example.libraryapplication.dto.BookDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookService {
    Optional<Book> getBookById(Long id);
    List<Book> getAllBooks();
    void createBook(BookDTO bookDTO);
    void deleteBook(Long bookId);
    void editBook(Long bookId, BookDTO bookDTO);
}
