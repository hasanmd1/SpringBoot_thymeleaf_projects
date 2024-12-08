package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.AuthorDAO;
import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.dto.AuthorDTO;
import com.example.libraryapplication.service.serviceImplementation.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorServiceImplTest {

    @Mock
    private AuthorDAO authorDAO;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private AuthorDTO authorDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        author = new Author();
        author.setId(1L);
        author.setFirstName("John");
        author.setLastName("Doe");

        authorDTO = new AuthorDTO();
        authorDTO.setFirstName("Jane");
        authorDTO.setLastName("Doe");
    }

    @Test
    void testGetAuthorById() {
        
        Long authorId = 1L;
        when(authorDAO.getAuthorById(authorId)).thenReturn(Optional.of(author));

        
        Optional<Author> result = authorService.getAuthorById(authorId);

        
        assertTrue(result.isPresent(), "Author should be present");
        assertEquals(author, result.get(), "The author should match the mock object");
        verify(authorDAO, times(1)).getAuthorById(authorId); 
    }

    @Test
    void testGetAuthorByIdNotFound() {
        
        Long authorId = 1L;
        when(authorDAO.getAuthorById(authorId)).thenReturn(Optional.empty());

        
        Optional<Author> result = authorService.getAuthorById(authorId);

        
        assertFalse(result.isPresent(), "Author should not be present");
        verify(authorDAO, times(1)).getAuthorById(authorId); 
    }

    @Test
    void testGetAllAuthors() {
        
        Author author2 = new Author();
        author2.setId(2L);
        author2.setFirstName("Alice");
        author2.setLastName("Smith");

        when(authorDAO.getAllAuthors()).thenReturn(Arrays.asList(author, author2));

        
        List<Author> result = authorService.getAllAuthors();

        
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "There should be two authors");
        verify(authorDAO, times(1)).getAllAuthors(); 
    }

    @Test
    void testCreateAuthor() {
        authorService.createAuthor(authorDTO);

        ArgumentCaptor<Author> authorArgumentCaptor = ArgumentCaptor.forClass(Author.class);
        verify(authorDAO, times(1)).saveAuthor(authorArgumentCaptor.capture());

        Author capturedAuthor = authorArgumentCaptor.getValue();

        assertNotNull(capturedAuthor);
        assertEquals("Jane", capturedAuthor.getFirstName());
        assertEquals("Doe", capturedAuthor.getLastName());
    }

    @Test
    void testEditAuthor() {
        
        Long authorId = 1L;
        AuthorDTO updatedAuthorDTO = new AuthorDTO();
        updatedAuthorDTO.setFirstName("Updated");
        updatedAuthorDTO.setLastName("Author");

        when(authorDAO.getAuthorById(authorId)).thenReturn(Optional.of(author));

        
        authorService.editAuthor(authorId, updatedAuthorDTO);

        
        assertEquals("Updated", author.getFirstName(), "The first name should be updated");
        assertEquals("Author", author.getLastName(), "The last name should be updated");
        verify(authorDAO, times(1)).saveAuthor(author); 
    }

    @Test
    void testEditAuthorNotFound() {
        
        Long authorId = 1L;
        AuthorDTO updatedAuthorDTO = new AuthorDTO();
        updatedAuthorDTO.setFirstName("Updated");
        updatedAuthorDTO.setLastName("Author");

        when(authorDAO.getAuthorById(authorId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.editAuthor(authorId, updatedAuthorDTO);
        });

        assertEquals("Author not found", exception.getMessage(), "Exception message should match");
        verify(authorDAO, times(0)).saveAuthor(any(Author.class));
    }

    @Test
    void testDeleteAuthor() {
        
        Long authorId = 1L;
        when(authorDAO.getAuthorById(authorId)).thenReturn(Optional.of(author));

        
        authorService.deleteAuthor(authorId);

        
        verify(authorDAO, times(1)).deleteAuthor(author); 
    }

    @Test
    void testDeleteAuthorNotFound() {
        
        Long authorId = 1L;
        when(authorDAO.getAuthorById(authorId)).thenReturn(Optional.empty());

        
        authorService.deleteAuthor(authorId);

        
        verify(authorDAO, times(0)).deleteAuthor(any(Author.class));
    }
}
