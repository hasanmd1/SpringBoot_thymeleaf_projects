package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.AuthorDAO;
import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.repository.AuthorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorDAOImpl implements AuthorDAO {

    private final AuthorRepository authorRepository;

    public AuthorDAOImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll().stream().toList();
    }

    @Override
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    @Override
    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    @Override
    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }
}
