package com.example.libraryapplication;

import com.example.libraryapplication.configuration.AuthenticationProviderService;
import com.example.libraryapplication.configuration.CustomUserDetails;
import com.example.libraryapplication.configuration.CustomUserDetailsService;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.repository.UserRepository;
import com.example.libraryapplication.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class LibraryApplicationTests {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @InjectMocks
    private AuthenticationProviderService authenticationProviderService;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetUserById() {
        Long userId = 1L;
        User mockUser = new User(userId, "johndoe", "passwordHash", "ADMIN", "jogn.doe@example.com", "John", "Doe", "123 Main St, New York, NY, 10001", "123-456-7890", null, new ArrayList<>());
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        User user = userService.getUserById(userId).orElseGet(User::new);
        assertNotNull(user);
        assertEquals(userId, user.getId());
        assertEquals("John Doe", user.getFirstName() + " " + user.getLastName());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testAuthenticationProviderInvalidUser() {
        String username = "john.doe@example.com";
        String password = "wrongPassword";
        User mockUser = new User(1L, "johndoe", "passwordHash", "ADMIN", "jogn.doe@example.com", "John", "Doe", "123 Main St, New York, NY, 10001", "123-456-7890", null, new ArrayList<>());
        CustomUserDetails customUserDetails = new CustomUserDetails(mockUser);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        when(passwordEncoder.matches(password, customUserDetails.getPassword())).thenReturn(false);
        assertThrows(NullPointerException.class, () -> {
            authenticationProviderService.authenticate(token);
        });
    }

    @Test
    void testRepositoryWorks() {
        User mockUser = new User(1L, "johndoe", "passwordHash", "ADMIN", "jogn.doe@example.com", "John", "Doe", "123 Main St, New York, NY, 10001", "123-456-7890", null, new ArrayList<>());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        User savedUser = userRepository.save(mockUser);
        assertNotNull(savedUser.getId());
        assertEquals("John Doe", savedUser.getFirstName() + " " + savedUser.getLastName());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testUserServiceCreateUser() {
        User mockUser = new User(1L, "johndoe", "passwordHash", "ADMIN", "jogn.doe@example.com", "John", "Doe", "123 Main St, New York, NY, 10001", "123-456-7890", null, new ArrayList<>());
        when(userRepository.save(mockUser)).thenReturn(mockUser);
        UserDTO userDTO = new UserDTO("johndoe", "passwordHash", "jogn.doe@example.com", "John", "Doe", "123 Main St, New York, NY, 10001", "123-456-7890", "ADMIN", null);
        User savedUser = userService.createUser(userDTO);
        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getFirstName() + " " + savedUser.getLastName());
        assertEquals("jogn.doe@example.com", savedUser.getEmail());
    }
}
