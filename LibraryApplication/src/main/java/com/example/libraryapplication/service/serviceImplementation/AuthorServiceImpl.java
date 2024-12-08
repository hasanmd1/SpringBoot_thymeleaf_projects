package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.AuthorDAO;
import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.dto.AuthorDTO;
import com.example.libraryapplication.service.AuthorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorDAO authorDAO;

    public AuthorServiceImpl(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public Optional<Author> getAuthorById(Long id) {
        return authorDAO.getAuthorById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorDAO.getAllAuthors();
    }

    @Override
    public void createAuthor(AuthorDTO authorDTO) {
        Author author = new Author();
        author.setFirstName(authorDTO.getFirstName());
        author.setLastName(authorDTO.getLastName());
        authorDAO.saveAuthor(author);
    }

    @Override
    public void editAuthor(Long id, AuthorDTO authorDTO) {
        Optional<Author> author = authorDAO.getAuthorById(id);
        if (author.isPresent()) {
            author.get().setFirstName(authorDTO.getFirstName());
            author.get().setLastName(authorDTO.getLastName());
            authorDAO.saveAuthor(author.get());
        } else {
            throw new RuntimeException("Author not found");
        }

    }

    @Override
    public void deleteAuthor(Long id) {
        Optional<Author> author = authorDAO.getAuthorById(id);
        author.ifPresent(authorDAO::deleteAuthor);
    }
}
