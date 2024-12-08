package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.UserDAO;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.service.serviceImplementation.AuthorizationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthorizationServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");
    }

    @Test
    void testAuthenticate_Success() {
        
        String username = "testuser";
        String password = "plainPassword";

        
        when(userDAO.getUserByUsername(username)).thenReturn(java.util.Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(true);

        
        assertTrue(authorizationService.authenticate(username, password));
    }

    @Test
    void testAuthenticate_Failure_WrongPassword() {
        
        String username = "testuser";
        String password = "wrongPassword";

        
        when(userDAO.getUserByUsername(username)).thenReturn(java.util.Optional.of(mockUser));
        when(passwordEncoder.matches(password, mockUser.getPassword())).thenReturn(false);

        
        assertFalse(authorizationService.authenticate(username, password));
    }

    @Test
    void testAuthenticate_Failure_UserNotFound() {
        
        String username = "testuser";
        String password = "somePassword";

        
        when(userDAO.getUserByUsername(username)).thenReturn(java.util.Optional.empty());

        
        assertFalse(authorizationService.authenticate(username, password));
    }

    @Test
    void testLogOut() {
        
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getName()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);

        
        authorizationService.logOut(request, response);

        
        verify(mockAuthentication, times(1)).getName();
    }

    @Test
    void testIsAuthenticated_WhenAuthenticated() {
        
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(true);
        when(mockAuthentication.getPrincipal()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);


        assertTrue(authorizationService.isAuthenticated());
    }

    @Test
    void testIsAuthenticated_WhenNotAuthenticated() {
        
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.isAuthenticated()).thenReturn(false);
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);


        assertFalse(authorizationService.isAuthenticated());
    }

    @Test
    void testIsAuthenticated_WhenAnonymousUser() {
        
        Authentication mockAuthentication = mock(Authentication.class);
        when(mockAuthentication.getPrincipal()).thenReturn("anonymousUser");
        SecurityContextHolder.getContext().setAuthentication(mockAuthentication);


        assertFalse(authorizationService.isAuthenticated());
    }
}
