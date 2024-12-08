package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDAO {
    List<Author> getAllAuthors();
    Optional<Author> getAuthorById(Long id);

    void saveAuthor(Author author);
    void deleteAuthor(Author author);
}
