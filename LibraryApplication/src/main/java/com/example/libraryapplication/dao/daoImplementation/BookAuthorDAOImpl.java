package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.BookAuthorDAO;
import com.example.libraryapplication.dataModel.BookAuthor;
import com.example.libraryapplication.repository.BookAuthorRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookAuthorDAOImpl implements BookAuthorDAO {

    private final BookAuthorRepository bookAuthorRepository;

    public BookAuthorDAOImpl(BookAuthorRepository bookAuthorRepository) {
        this.bookAuthorRepository = bookAuthorRepository;
    }

    @Override
    public List<BookAuthor> findByBookIdAndAuthorId(Long bookId, Long authorId) {
        return bookAuthorRepository.findByBookIdAndAuthorId(bookId, authorId);
    }

    @Override
    public List<BookAuthor> findByBookId(Long bookId) {
        return bookAuthorRepository.findByBookId(bookId);
    }

    @Override
    public List<BookAuthor> findByAuthorId(Long authorId) {
        return bookAuthorRepository.findByAuthorId(authorId);
    }

    @Override
    public List<BookAuthor> findAll() {
        return bookAuthorRepository.findAll();
    }

    @Override
    public void saveBookAuthor(BookAuthor bookAuthor) {
        bookAuthorRepository.save(bookAuthor);
    }

    @Override
    public void deleteBookAuthor(BookAuthor bookAuthor) {
        bookAuthorRepository.delete(bookAuthor);
    }
}
