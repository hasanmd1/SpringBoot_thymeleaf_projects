package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.*;
import com.example.libraryapplication.dataModel.*;
import com.example.libraryapplication.dto.BookDTO;
import com.example.libraryapplication.service.serviceImplementation.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private AuthorDAO authorDAO;

    @Mock
    private GenreDAO genreDAO;

    @Mock
    private BookCopyDAO bookCopyDAO;

    @Mock
    private BookAuthorDAO bookAuthorDAO;

    @Mock
    private BookGenreDAO bookGenreDAO;

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookDAO, authorDAO, genreDAO, bookCopyDAO, bookAuthorDAO, bookGenreDAO);
    }

    @Test
    void testCreateBookSuccessfully() {
        
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        bookDTO.setPublisher("Publisher");
        bookDTO.setPublicationDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bookDTO.setISBN("123-456-789");
        bookDTO.setNumberOfCopies(5L);
        bookDTO.setBookAuthorIds(Arrays.asList(1L, 2L));
        bookDTO.setGenreIds(Arrays.asList(1L, 2L));

        Author author1 = new Author();
        author1.setId(1L);
        Author author2 = new Author();
        author2.setId(2L);
        List<Author> authors = Arrays.asList(author1, author2);

        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(2L);
        List<Genre> genres = Arrays.asList(genre1, genre2);

        
        when(authorDAO.getAllAuthors()).thenReturn(authors);
        when(genreDAO.getAllGenres()).thenReturn(genres);

        
        bookService.createBook(bookDTO);

        
        verify(bookDAO, times(1)).saveBook(any(Book.class));
    }

    @Test
    void testCreateBookWithMissingAuthors() {
        
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("New Book");
        bookDTO.setPublisher("Publisher");
        bookDTO.setPublicationDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bookDTO.setISBN("123-456-789");
        bookDTO.setNumberOfCopies(5L);
        bookDTO.setBookAuthorIds(Arrays.asList(1L, 999L));

        Author author1 = new Author();
        author1.setId(1L);
        List<Author> authors = Collections.singletonList(author1);

        
        when(authorDAO.getAllAuthors()).thenReturn(authors);

         
        assertThrows(IllegalArgumentException.class, () -> bookService.createBook(bookDTO));
    }

    @Test
    void testEditBookSuccessfully() {
        
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");
        bookDTO.setPublisher("Updated Publisher");
        bookDTO.setPublicationDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        bookDTO.setISBN("987-654-321");
        bookDTO.setNumberOfCopies(3L);
        bookDTO.setBookAuthorIds(Arrays.asList(1L)); 
        bookDTO.setGenreIds(Arrays.asList(1L)); 

        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Book");

        Author author1 = new Author();
        author1.setId(1L);
        List<Author> authors = Collections.singletonList(author1);

        Genre genre1 = new Genre();
        genre1.setId(1L);
        List<Genre> genres = Collections.singletonList(genre1);

        
        when(bookDAO.getBook(1L)).thenReturn(Optional.of(existingBook));
        when(authorDAO.getAllAuthors()).thenReturn(authors);
        when(genreDAO.getAllGenres()).thenReturn(genres);

        
        bookService.editBook(1L, bookDTO);

        
        assertEquals("Updated Book", existingBook.getTitle());
        assertEquals("Updated Publisher", existingBook.getPublisher());
        assertEquals("987-654-321", existingBook.getISBN());
        verify(bookDAO, times(1)).saveBook(existingBook);
    }

    @Test
    void testEditBookWithInvalidNumberOfCopies() {
        
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");
        bookDTO.setNumberOfCopies(-1L);  

         
        assertThrows(IllegalArgumentException.class, () -> bookService.editBook(1L, bookDTO));
    }

    @Test
    void testDeleteBook() {
        
        Long bookId = 1L;

        
        bookService.deleteBook(bookId);

        
        verify(bookDAO, times(1)).deleteBook(bookId);
    }
}
