package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.GenreDAO;
import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.dto.GenreDTO;
import com.example.libraryapplication.service.GenreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDAO genreDAO;

    public GenreServiceImpl(GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        return genreDAO.getGenreById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreDAO.getAllGenres();
    }

    @Override
    public void createGenre(GenreDTO genreDTO) {
        Genre genre = new Genre();
        genre.setName(genreDTO.getName());
        genreDAO.saveGenre(genre);

    }

    @Override
    public void deleteGenre(Long genreId) {
        genreDAO.deleteGenre(genreId);
    }

    @Override
    public void editGenre(Long genreId, GenreDTO genreDTO) {
        Genre genre = genreDAO.getGenreById(genreId).orElseGet(Genre::new);
        genre.setName(genreDTO.getName());
        genreDAO.saveGenre(genre);
    }
}
