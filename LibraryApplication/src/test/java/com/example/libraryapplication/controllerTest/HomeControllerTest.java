package com.example.libraryapplication.controllerTest;

import com.example.libraryapplication.configuration.CustomUserDetails;
import com.example.libraryapplication.controllers.HomeController;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.service.AuthorizationService;
import com.example.libraryapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HomeControllerTest {

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private HomeController homeController;

    private MockMvc mockMvc;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testHomeWhenUserIsAuthenticated() throws Exception {
        
        CustomUserDetails userDetails = new CustomUserDetails(mockUser);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails.getUsername());
        when(userService.getUserByUsername("testUser")).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attributeExists("authenticated", "user", "login"))
                .andExpect(model().attribute("authenticated", true))
                .andExpect(model().attribute("login", true))
                .andExpect(model().attribute("user", mockUser));
    }

    @Test
    void testHomeWhenUserIsNotAuthenticated() throws Exception {
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attributeExists("authenticated", "user", "login"))
                .andExpect(model().attribute("authenticated", false))
                .andExpect(model().attribute("login", false));
    }

    @Test
    void testHomeWhenPrincipalIsAnonymousUser() throws Exception {
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");

        
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attributeExists("authenticated", "user", "login"))
                .andExpect(model().attribute("authenticated", false))
                .andExpect(model().attribute("login", false));
    }

    @Test
    void testGetHomeRedirectsToHome() throws Exception {
        
        mockMvc.perform(get("/home"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}
