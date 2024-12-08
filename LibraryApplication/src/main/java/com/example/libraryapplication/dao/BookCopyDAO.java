package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.BookCopy;

import java.util.List;
import java.util.Optional;

public interface BookCopyDAO {
    List<BookCopy> getAllBookCopies();
    Optional<BookCopy> getBookCopyById(Long id);
    void saveBookCopy(BookCopy bookCopy);
    void deleteBookCopy(Long bookCopyId);
}
