package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.BookGenreDAO;
import com.example.libraryapplication.dataModel.BookGenre;
import com.example.libraryapplication.repository.BookGenreRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookGenreDAOImpl implements BookGenreDAO {

    public final BookGenreRepository bookGenreRepository;

    public BookGenreDAOImpl(BookGenreRepository bookGenreRepository) {
        this.bookGenreRepository = bookGenreRepository;
    }

    @Override
    public Optional<BookGenre> getBookGenre(Long id) {
        return bookGenreRepository.findById(id);
    }

    @Override
    public List<BookGenre> getAllBookGenres() {
        return bookGenreRepository.findAll();
    }

    @Override
    public List<BookGenre> getBookGenresByBookIdAndGenreId(Long bookId, Long genreId) {
        return bookGenreRepository.findAll().stream().filter(bookGenre -> bookGenre.getBook().getId().equals(bookId) && bookGenre.getGenre().getId().equals(bookId)).toList();
    }

    @Override
    public void deleteGenre(Long id) {
        bookGenreRepository.deleteById(id);
    }

    @Override
    public void saveGenre(BookGenre bookGenre) {
        bookGenreRepository.save(bookGenre);
    }
}
