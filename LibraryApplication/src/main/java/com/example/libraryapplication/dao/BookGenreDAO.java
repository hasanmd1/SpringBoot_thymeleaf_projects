package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.BookGenre;

import java.util.List;
import java.util.Optional;

public interface BookGenreDAO {
    Optional <BookGenre> getBookGenre(Long id);
    List<BookGenre> getAllBookGenres();
    List<BookGenre> getBookGenresByBookIdAndGenreId(Long bookId, Long genreId);
    void deleteGenre(Long id);
    void saveGenre(BookGenre bookGenre);
}
