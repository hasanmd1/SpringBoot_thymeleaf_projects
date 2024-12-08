package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.GenreDAO;
import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.dto.GenreDTO;
import com.example.libraryapplication.service.serviceImplementation.GenreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GenreServiceTest {

    @Mock
    private GenreDAO genreDAO;

    @InjectMocks
    private GenreServiceImpl genreService;

    private GenreDTO genreDTO;
    private Genre genre;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize sample GenreDTO and Genre
        genreDTO = new GenreDTO();
        genreDTO.setName("Science Fiction");

        genre = new Genre();
        genre.setId(1L);
        genre.setName("Science Fiction");
    }

    @Test
    void testCreateGenre() {
        
        genreService.createGenre(genreDTO);

        
        verify(genreDAO, times(1)).saveGenre(any(Genre.class));
    }

    @Test
    void testGetGenreById() {
        
        when(genreDAO.getGenreById(1L)).thenReturn(Optional.of(genre));

        
        Optional<Genre> result = genreService.getGenreById(1L);

        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Science Fiction", result.get().getName());
    }

    @Test
    void testGetAllGenres() {
        
        Genre genre1 = new Genre();
        genre1.setId(1L);
        genre1.setName("Fiction");

        Genre genre2 = new Genre();
        genre2.setId(2L);
        genre2.setName("Non-Fiction");

        when(genreDAO.getAllGenres()).thenReturn(List.of(genre1, genre2));

        
        List<Genre> genres = genreService.getAllGenres();

        
        assertNotNull(genres);
        assertEquals(2, genres.size());
        assertEquals("Fiction", genres.get(0).getName());
        assertEquals("Non-Fiction", genres.get(1).getName());
    }

    @Test
    void testDeleteGenre() {
        
        genreService.deleteGenre(1L);

        
        verify(genreDAO, times(1)).deleteGenre(1L);
    }

    @Test
    void testEditGenre() {
        
        GenreDTO updatedGenreDTO = new GenreDTO();
        updatedGenreDTO.setName("Fantasy");

        when(genreDAO.getGenreById(1L)).thenReturn(Optional.of(genre));

        
        genreService.editGenre(1L, updatedGenreDTO);

        
        assertEquals("Fantasy", genre.getName());
        verify(genreDAO, times(1)).saveGenre(genre);
    }

    @Test
    void testEditGenreWhenNotFound() {

        GenreDTO updatedGenreDTO = new GenreDTO();
        updatedGenreDTO.setName("Fantasy");

        when(genreDAO.getGenreById(1L)).thenReturn(Optional.empty());

        genreService.editGenre(1L, updatedGenreDTO);

        Genre newGenre = new Genre();
        newGenre.setName("Fantasy");

        verify(genreDAO, times(1)).saveGenre(argThat(genre -> "Fantasy".equals(genre.getName())));
    }

}
