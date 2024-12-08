package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDAO {

    List<Genre> getAllGenres();
    Optional<Genre> getGenreById(Long id);


    void saveGenre(Genre genre);
    void deleteGenre(Long id);
}
