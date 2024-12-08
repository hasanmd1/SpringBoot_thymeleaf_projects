package com.example.libraryapplication.service;

import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.dto.AuthorDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AuthorService {
    Optional<Author> getAuthorById(Long id);
    List<Author> getAllAuthors();
    void createAuthor(AuthorDTO authorDTO);
    void editAuthor(Long id, AuthorDTO authorDTO);
    void deleteAuthor(Long id);

}
