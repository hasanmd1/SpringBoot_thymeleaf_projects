package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.BookDAO;
import com.example.libraryapplication.dataModel.Book;
import com.example.libraryapplication.repository.BookRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookDAOImpl implements BookDAO {

    private final BookRepository bookRepository;

    public BookDAOImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
