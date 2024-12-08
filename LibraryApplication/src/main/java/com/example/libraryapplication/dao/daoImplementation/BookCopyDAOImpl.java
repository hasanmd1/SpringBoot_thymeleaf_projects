package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.BookCopyDAO;
import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.repository.BookCopyRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookCopyDAOImpl implements BookCopyDAO {

    private final BookCopyRepository bookCopyRepository;

    public BookCopyDAOImpl(BookCopyRepository bookCopyRepository) {
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public List<BookCopy> getAllBookCopies() {
        return bookCopyRepository.findAll();
    }

    @Override
    public Optional<BookCopy> getBookCopyById(Long id) {
        return bookCopyRepository.findById(id);
    }

    @Override
    public void saveBookCopy(BookCopy bookCopy) {
        bookCopyRepository.save(bookCopy);
    }

    @Override
    public void deleteBookCopy(Long bookCopyId) {
        bookCopyRepository.deleteById(bookCopyId);
    }
}
