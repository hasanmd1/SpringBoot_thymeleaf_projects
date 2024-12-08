package com.example.libraryapplication.controllerTest;

import com.example.libraryapplication.controllers.GenreController;
import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.GenreDTO;
import com.example.libraryapplication.service.GenreService;
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

public class GenreControllerTest {

    @Mock
    private GenreService genreService;

    @Mock
    private UserService userService;

    @InjectMocks
    private GenreController genreController;

    private MockMvc mockMvc;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(genreController).build();

        
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("admin");
        mockUser.setRole("ADMIN");
    }

    @Test
    void testGetGenres() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(genreService.getAllGenres()).thenReturn(List.of());

        
        mockMvc.perform(get("/user/1/bookGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/displayBookGenre"))
                .andExpect(model().attributeExists("genres", "user"));
    }

    @Test
    void testGetCreateGenre() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/bookGenre/createBookGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/createBookGenre"))
                .andExpect(model().attributeExists("genre", "user"));
    }

    @Test
    void testCreateGenreSuccess() throws Exception {
        
        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setName("Fantasy");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(post("/user/1/bookGenre/createBookGenre")
                        .flashAttr("genre", genreDTO)
                        .flashAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/displayBookGenre"))
                .andExpect(model().attributeExists("genres", "user"));

        verify(genreService, times(1)).createGenre(genreDTO);
    }

    @Test
    void testCreateGenreWithValidationErrors() throws Exception {
        
        GenreDTO genreDTO = new GenreDTO(); // Missing name triggers validation error
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        
        mockMvc.perform(post("/user/1/bookGenre/createBookGenre")
                        .flashAttr("genre", genreDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/createBookGenre"))
                .andExpect(model().attributeExists("user"));

        verify(genreService, never()).createGenre(any());
    }

    @Test
    void testGetEditGenre() throws Exception {
        
        Genre mockGenre = new Genre();
        mockGenre.setId(1L);
        mockGenre.setName("Fantasy");

        when(genreService.getGenreById(1L)).thenReturn(Optional.of(mockGenre));
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/bookGenre/1/editBookGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/editBookGenre"))
                .andExpect(model().attributeExists("genre", "user", "genreDTO"));
    }

    @Test
    void testEditGenreSuccess() throws Exception {
        
        Genre mockGenre = new Genre();
        mockGenre.setId(1L);
        mockGenre.setName("Fantasy");

        GenreDTO genreDTO = new GenreDTO();
        genreDTO.setName("Updated Genre");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(genreService.getGenreById(1L)).thenReturn(Optional.of(mockGenre));

        
        mockMvc.perform(post("/user/1/bookGenre/1/editBookGenre")
                        .flashAttr("genreDTO", genreDTO)
                        .flashAttr("genre", mockGenre)
                        .flashAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/displayBookGenre"))
                .andExpect(model().attributeExists("genreDTO", "user"));

        verify(genreService, times(1)).editGenre(1L, genreDTO);
    }

    @Test
    void testDeleteGenreSuccess() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/bookGenre/1/deleteBookGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/displayBookGenre"))
                .andExpect(model().attributeExists("genres", "user"));

        verify(genreService, times(1)).deleteGenre(1L);
    }

    @Test
    void testDeleteGenreNotFound() throws Exception {
        
        doNothing().when(genreService).deleteGenre(1L);
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/user/1/bookGenre/1/deleteBookGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("BookGenre/displayBookGenre"));


        verify(genreService, times(0)).deleteGenre(1L);
    }
}
