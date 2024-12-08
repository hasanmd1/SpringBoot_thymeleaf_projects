package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.BookCopyDAO;
import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.service.BookCopyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookCopyServiceImpl implements BookCopyService {
    private final BookCopyDAO bookCopyDAO;

    public BookCopyServiceImpl(BookCopyDAO bookCopyDAO) {
        this.bookCopyDAO = bookCopyDAO;
    }

    @Override
    public Optional<BookCopy> getBookCopyById(Long id) {
        return bookCopyDAO.getBookCopyById(id);
    }

    @Override
    public List<BookCopy> getAllBookCopies() {
        return bookCopyDAO.getAllBookCopies();
    }
}
