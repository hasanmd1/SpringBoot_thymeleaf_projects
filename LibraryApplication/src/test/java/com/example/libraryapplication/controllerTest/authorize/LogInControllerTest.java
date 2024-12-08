package com.example.libraryapplication.controllerTest.authorize;

import com.example.libraryapplication.controllers.authorization.LogInController;
import com.example.libraryapplication.dataModel.TokenInfo;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.PasswordResetDTO;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.AuthorizationService;
import com.example.libraryapplication.service.UserService;
import com.example.libraryapplication.configuration.EmailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LogInControllerTest {

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private UserService userService;

    @Mock
    private EmailSender emailSender;

    @Mock
    private Map<String, TokenInfo> tokenStore;

    @InjectMocks
    private LogInController logInController;

    private MockMvc mockMvc;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(logInController).build();
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setEmail("test@example.com");
    }

    @Test
    void testGetLogIn() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/logInPage"));
    }

    @Test
    void testGetForgotPassword() throws Exception {
        mockMvc.perform(get("/forgotPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/forgotPassword"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void testForgotPasswordValid() throws Exception {
        UserDTO userDTO = new UserDTO("testUser", "password", "test@example.com", "Test", "User", "Address", "123456789", "USER", null);
        when(userService.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/forgotPassword")
                        .flashAttr("userDTO", userDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/linkSent"))
                .andExpect(model().attribute("user", userDTO));

        verify(emailSender, times(1)).sendPasswordResetUrl(eq(userDTO.getEmail()), anyString());
    }

    @Test
    void testForgotPasswordInvalid() throws Exception {
        UserDTO userDTO = new UserDTO("testUser", "password", "invalid@example.com", "Test", "User", "Address", "123456789", "USER", null);
        when(userService.getUserByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

        mockMvc.perform(post("/forgotPassword")
                        .flashAttr("userDTO", userDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/forgotPassword"))
                .andExpect(model().attribute("invalid", true));

        verify(emailSender, never()).sendPasswordResetUrl(eq(userDTO.getEmail()), anyString());
    }

    @Test
    void testRedirectToResetPassword() throws Exception {
        String token = "validToken";
        TokenInfo tokenInfo = new TokenInfo(token, LocalDateTime.now().plusMinutes(10));  // token expiry time in the future
        String email = "test@example.com";

        tokenStore.put(email, tokenInfo);

        PasswordResetDTO passwordResetDTO = new PasswordResetDTO();

        
        mockMvc.perform(get("/forgotPassword/redirect")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/forgotPassword"))
                .andExpect(model().attributeExists("passwordResetDTO", "invalidToken", "user"));
    }


    @Test
    void testRedirectToResetPasswordInvalidToken() throws Exception {
        String token = "invalidToken";
        mockMvc.perform(get("/forgotPassword/redirect")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/forgotPassword"))
                .andExpect(model().attribute("invalidToken", true));
    }

    @Test
    void testResetPassword() throws Exception {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO("encodedPassword","newPassword", "newPassword");
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/resetPassword")
                        .param("userId", "1")
                        .flashAttr("passwordResetDTO", passwordResetDTO))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(userService, times(1)).resetPassword(eq(mockUser), eq(passwordResetDTO));
    }

    @Test
    void testResetPasswordInvalid() throws Exception {
        PasswordResetDTO passwordResetDTO = new PasswordResetDTO("low","newPassword", "differentPassword");
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/resetPassword")
                        .param("userId", "1")
                        .flashAttr("passwordResetDTO", passwordResetDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("LogIn/resetPassword"))
                .andExpect(model().attribute("invalid", true));
    }

    @Test
    void testLogOut() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().isOk())
                .andExpect(view().name("Home"))
                .andExpect(model().attribute("logout", true));

        verify(authorizationService, times(1)).logOut(any(), any());
    }
}
