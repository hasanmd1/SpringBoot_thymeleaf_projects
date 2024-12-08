package com.example.libraryapplication.controllerTest;

import com.example.libraryapplication.controllers.UserController;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private GenreService genreService;

    @Mock
    private ReservationService reservationService;

    @Mock
    private BookService bookService;

    @Mock
    private AuthorService authorService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setRole("ADMIN");
        mockUser.setEmail("test@example.com");
    }

    @Test
    void testGetUser() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(genreService.getAllGenres()).thenReturn(List.of());
        when(bookService.getAllBooks()).thenReturn(List.of());
        when(reservationService.getAllReservations()).thenReturn(List.of());

       
        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("User/displayUser"))
                .andExpect(model().attributeExists("user", "genre", "author", "books", "reservations"));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testGetEditUser() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        UserDTO userDTO = new UserDTO("testUser", null, "test@example.com", null, null, null, null, "ADMIN", null);

       
        mockMvc.perform(get("/user/1/editUser")
                        .flashAttr("userDTO", userDTO)
                        .flashAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(view().name("User/editUser"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("userDTO", userDTO));

        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void testEditUser() throws Exception {
        
        UserDTO updatedUserDTO = new UserDTO("updatedUser", "newPassword", "updated@example.com", "Updated", "User", "New Address", "987654321", "USER", null);
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(userService.updateUser(updatedUserDTO, 1L)).thenReturn(mockUser);

       
        mockMvc.perform(post("/user/1/editUser")
                        .flashAttr("userDTO", updatedUserDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("User/displayUser"))
                .andExpect(model().attribute("user", mockUser));

        verify(userService, times(1)).updateUser(updatedUserDTO, 1L);
    }

    @Test
    void testSearchUser() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(userService.getAllUsersByValue("test")).thenReturn(List.of(mockUser));

       
        mockMvc.perform(get("/user/1/searchUser").param("search", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("User/searchUser"))
                .andExpect(model().attributeExists("userList"));

        verify(userService, times(1)).getAllUsersByValue("test");
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

       
        mockMvc.perform(get("/user/1/deleteUser"))
                .andExpect(status().isOk())
                .andExpect(view().name("User/displayUser"));

    }
}
