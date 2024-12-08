package com.example.libraryapplication.service;

import com.example.libraryapplication.dataModel.BookCopy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BookCopyService {
    Optional<BookCopy> getBookCopyById(Long id);
    List<BookCopy> getAllBookCopies();
}
