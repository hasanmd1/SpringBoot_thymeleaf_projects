package com.example.libraryapplication.controllerTest;

import com.example.libraryapplication.controllers.BookController;
import com.example.libraryapplication.dataModel.Book;
import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.BookDTO;
import com.example.libraryapplication.service.AuthorService;
import com.example.libraryapplication.service.BookService;
import com.example.libraryapplication.service.GenreService;
import com.example.libraryapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private GenreService genreService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private BookController bookController;

    private MockMvc mockMvc;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setRole("ADMIN");
    }

    @Test
    void testGetBook() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(bookService.getAllBooks()).thenReturn(List.of());

        
        mockMvc.perform(get("/user/1/book"))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/displayBook"))
                .andExpect(model().attributeExists("user", "books"));
    }

    @Test
    void testGetCreateBook() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(genreService.getAllGenres()).thenReturn(List.of());
        when(authorService.getAllAuthors()).thenReturn(List.of());

        
        mockMvc.perform(get("/user/1/book/createBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/createBook"))
                .andExpect(model().attributeExists("user", "book", "genres", "authors"));
    }

    @Test
    void testCreateBookSuccess() throws Exception {
        
        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Sample Book");
        bookDTO.setISBN("1234567890");
        bookDTO.setNumberOfCopies(5L);

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(post("/user/1/book/createBook")
                        .flashAttr("book", bookDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/displayBook"))
                .andExpect(model().attributeExists("user", "books"));

        verify(bookService, times(1)).createBook(bookDTO);
    }

    @Test
    void testCreateBookWithValidationErrors() throws Exception {
        
        BookDTO bookDTO = new BookDTO();
        bookDTO.setNumberOfCopies(-5L);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));


        
        mockMvc.perform(post("/user/1/book/createBook")
                        .flashAttr("book", bookDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/createBook"))
                .andExpect(model().attributeExists("user", "genres", "authors", "book", "invalid"));

        verify(bookService, never()).createBook(any());
    }

    @Test
    void testGetEditBook() throws Exception {
        
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));
        when(genreService.getAllGenres()).thenReturn(List.of());
        when(authorService.getAllAuthors()).thenReturn(List.of());

        
        mockMvc.perform(get("/user/1/book/1/editBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/editBook"))
                .andExpect(model().attributeExists("user", "book", "bookDTO", "genres", "authors", "authorMap", "genreMap"));
    }

    @Test
    void testEditBookSuccess() throws Exception {
        
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Book");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        
        mockMvc.perform(post("/user/1/book/1/editBook")
                        .flashAttr("bookDTO", bookDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/displayBook"))
                .andExpect(model().attributeExists("user", "books"));

        verify(bookService, times(1)).editBook(1L, bookDTO);
    }

    @Test
    void testEditBookWithValidationErrors() throws Exception {
        
        BookDTO bookDTO = new BookDTO(); // Invalid DTO to trigger errors
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(bookService.getBookById(1L)).thenReturn(Optional.empty());

        
        mockMvc.perform(post("/user/1/book/1/editBook")
                        .flashAttr("bookDTO", bookDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/editBook"))
                .andExpect(model().attributeExists("user", "book", "genres", "authors", "invalid"));

        verify(bookService, never()).editBook(anyLong(), any());
    }

    @Test
    void testDeleteBook() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/book/1/deleteBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("Book/displayBook"))
                .andExpect(model().attributeExists("user", "books"));

        verify(bookService, times(1)).deleteBook(1L);
    }
}
