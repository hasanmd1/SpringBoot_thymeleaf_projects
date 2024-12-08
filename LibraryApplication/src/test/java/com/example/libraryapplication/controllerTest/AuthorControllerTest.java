package com.example.libraryapplication.controllerTest;

import com.example.libraryapplication.controllers.AuthorController;
import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.AuthorDTO;
import com.example.libraryapplication.service.AuthorService;
import com.example.libraryapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AuthorControllerTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthorController authorController;

    private MockMvc mockMvc;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authorController).build();
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setRole("ADMIN");
    }

    @Test
    void testGetAuthor() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(authorService.getAllAuthors()).thenReturn(List.of());

        
        mockMvc.perform(get("/user/1/author"))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/displayAuthor"))
                .andExpect(model().attributeExists("authors", "user"));
    }

    @Test
    void testGetCreateAuthor() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/author/createAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/createAuthor"))
                .andExpect(model().attributeExists("author", "users", "user"));
    }

    @Test
    void testCreateAuthor() throws Exception {
        
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName("John");
        authorDTO.setLastName("Doe");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        
        mockMvc.perform(post("/user/1/author/createAuthor")
                        .flashAttr("author", authorDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/displayAuthor"))
                .andExpect(model().attributeExists("authors", "user"));

        
        verify(authorService, times(1)).createAuthor(authorDTO);
    }

    @Test
    void testCreateAuthorWithValidationErrors() throws Exception {
        
        AuthorDTO authorDTO = new AuthorDTO();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(post("/user/1/author/createAuthor")
                        .flashAttr("author", authorDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/displayAuthor"))
                .andExpect(model().attributeExists("authors", "user"));
    }

    @Test
    void testGetEditAuthor() throws Exception {
        
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName("John");
        authorDTO.setLastName("Doe");
        when(authorService.getAuthorById(1L)).thenReturn(Optional.of(new Author()));
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/author/1/editAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/editAuthor"))
                .andExpect(model().attributeExists("author", "users", "user", "authorDTO"));
    }

    @Test
    void testEditAuthor() throws Exception {
        
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName("John");
        authorDTO.setLastName("Doe");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(post("/user/1/author/1/editAuthor")
                        .flashAttr("authorDTO", authorDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/displayAuthor"))
                .andExpect(model().attributeExists("authors", "user"));

        
        verify(authorService, times(1)).editAuthor(1L, authorDTO);
    }

    @Test
    void testDeleteAuthor() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/author/1/deleteAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("Author/displayAuthor"))
                .andExpect(model().attributeExists("authors", "user"));

        
        verify(authorService, times(1)).deleteAuthor(1L);
    }
}
