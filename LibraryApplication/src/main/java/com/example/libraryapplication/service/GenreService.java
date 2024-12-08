package com.example.libraryapplication.service;

import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.dto.GenreDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface GenreService {
    Optional<Genre> getGenreById(Long id);
    List<Genre> getAllGenres();
    void createGenre(GenreDTO genreDTO);
    void deleteGenre(Long genreId);
    void editGenre(Long genreId, GenreDTO genreDTO);
}
