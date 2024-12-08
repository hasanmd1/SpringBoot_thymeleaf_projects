package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.GenreDAO;
import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.repository.GenreRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDAOImpl implements GenreDAO {

    private final GenreRepository genreRepository;

    public GenreDAOImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @Override
    public Optional<Genre> getGenreById(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public void saveGenre(Genre genre) {
        genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }
}
