package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.UserDAO;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.PasswordResetDTO;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.serviceImplementation.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private PasswordResetDTO passwordResetDTO;

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Prepare mock user and DTOs
        user = new User();
        user.setId(1L);
        user.setUsername("john.doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");

        userDTO = new UserDTO();
        userDTO.setUsername("john.doe");
        userDTO.setPassword("newPassword");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setAddress("123 Main St");
        userDTO.setPhoneNumber("123-456-7890");

        passwordResetDTO = new PasswordResetDTO();
        passwordResetDTO.setCurrentPassword("encodedPassword");
        passwordResetDTO.setNewPassword("newPassword");
        passwordResetDTO.setConfirmNewPassword("newPassword");
    }

    @AfterEach
    void tearDown() {
        user = null;
        userDTO = null;
        passwordResetDTO = null;
    }

    @Test
    void testCreateUserSuccess() {
        
        when(userDAO.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(userDAO.getUserByPhoneNumber(userDTO.getPhoneNumber())).thenReturn(Optional.empty());
        when(userDAO.getUserByUsername(userDTO.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        
        User createdUser = userService.createUser(userDTO);

        
        assertNotNull(createdUser);
        assertEquals(userDTO.getUsername(), createdUser.getUsername());
        verify(userDAO, times(1)).saveUser(createdUser);
    }

    @Test
    void testCreateUserWithExistingEmail() {
        
        when(userDAO.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(user));

        
        User createdUser = userService.createUser(userDTO);

        
        assertNull(createdUser);
        verify(userDAO, never()).saveUser(any());
    }

    @Test
    void testGetUserById() {
        
        when(userDAO.getUserById(1L)).thenReturn(Optional.of(user));

        
        Optional<User> foundUser = userService.getUserById(1L);

        
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
    }

    @Test
    void testResetPasswordSuccess() {
        
        when(userDAO.getUserById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(passwordResetDTO.getNewPassword())).thenReturn("newEncodedPassword");

        
        userService.resetPassword(user, passwordResetDTO);

        
        assertEquals("newEncodedPassword", user.getPassword());
        verify(userDAO, times(1)).saveUser(user);
    }

    @Test
    void testResetPasswordFailure() {
        
        when(userDAO.getUserById(1L)).thenReturn(Optional.of(user));

        
        passwordResetDTO.setNewPassword("newPassword");
        passwordResetDTO.setConfirmNewPassword("differentPassword");
        userService.resetPassword(user, passwordResetDTO);

        
        assertNotEquals("newPassword", user.getPassword());
        verify(userDAO, times(0)).saveUser(user);
    }

    @Test
    void testUpdateUser() {
        
        UserDTO updateDTO = new UserDTO();
        updateDTO.setFirstName("Updated Name");
        updateDTO.setPassword("newPassword");
        when(userDAO.getUserById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updateDTO.getPassword())).thenReturn("encodedNewPassword");

        
        User updatedUser = userService.updateUser(updateDTO, 1L);

        
        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getFirstName());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
        verify(userDAO, times(1)).saveUser(updatedUser);
    }

    @Test
    void testDeleteUser() {
        
        when(userDAO.getUserById(1L)).thenReturn(Optional.of(user));

        
        userService.deleteUser(1L);

        
        verify(userDAO, times(1)).deleteUser(1L);
    }
}
