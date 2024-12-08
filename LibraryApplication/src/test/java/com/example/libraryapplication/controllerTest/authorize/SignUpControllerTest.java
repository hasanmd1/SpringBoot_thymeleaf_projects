package com.example.libraryapplication.controllerTest.authorize;

import com.example.libraryapplication.configuration.EmailSender;
import com.example.libraryapplication.controllers.authorization.SignUpController;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.UserService;
import com.example.libraryapplication.dataModel.TokenInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class SignUpControllerTest {

    @Autowired
    private SignUpController signUpController;

    @MockBean
    private UserService userService;

    @MockBean
    private EmailSender emailService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(signUpController).build();
    }

    private void setTokenStore(String email, TokenInfo tokenInfo) throws NoSuchFieldException, IllegalAccessException {
        Field field = SignUpController.class.getDeclaredField("tokenStore");
        field.setAccessible(true);
        @SuppressWarnings("unchecked")
        var tokenStore = (java.util.Map<String, TokenInfo>) field.get(signUpController);
        tokenStore.put(email, tokenInfo);
    }

    @Test
    public void testGetSignUpPage() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("SignUp/signUpPage"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testSignUpWithExistingUsernameOrEmail() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUsername");
        userDTO.setEmail("existingEmail@example.com");

        Mockito.when(userService.getUserByUsername("existingUsername")).thenReturn(Optional.of(new User()));
        Mockito.when(userService.getUserByEmail("existingEmail@example.com")).thenReturn(Optional.of(new User()));

        mockMvc.perform(post("/signup")
                        .flashAttr("user", userDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("SignUp/signUpPage"))
                .andExpect(model().attribute("invalid", true));
    }

    @Test
    public void testSignUpAndEmailVerification() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("newEmail@example.com");

        Mockito.when(userService.getUserByEmail("newEmail@example.com")).thenReturn(Optional.empty());
        Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(new User());

        mockMvc.perform(post("/signup")
                        .flashAttr("user", userDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("SignUp/emailVerification"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testVerifyEmailWithValidToken() throws Exception {
        String token = UUID.randomUUID().toString();
        TokenInfo tokenInfo = new TokenInfo(token, LocalDateTime.now().plusHours(24));
        String email = "newEmail@example.com";

        setTokenStore(email, tokenInfo);
        Mockito.when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));

        mockMvc.perform(get("/signup/emailVerification")
                        .param("token", token))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testVerifyEmailWithExpiredToken() throws Exception {
        String token = UUID.randomUUID().toString();
        TokenInfo tokenInfo = new TokenInfo(token, LocalDateTime.now().minusHours(1)); // Expired token
        String email = "expiredToken@example.com";

        setTokenStore(email, tokenInfo);

        mockMvc.perform(get("/signup/emailVerification")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(view().name("SignUp/signUpPage"))
                .andExpect(model().attribute("invalid", true))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    public void testResendVerificationEmail() throws Exception {
        String email = "userToResend@example.com";
        String token = UUID.randomUUID().toString();
        setTokenStore(email, new TokenInfo(token, LocalDateTime.now().plusHours(24)));

        Mockito.when(userService.getUserByEmail(email)).thenReturn(Optional.of(new User()));

        mockMvc.perform(get("/signup/resendVerification")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(view().name("SignUp/emailVerification"));
    }

    @Test
    public void testSignUpWithInvalidInput() throws Exception {
        UserDTO userDTO = new UserDTO();  

        mockMvc.perform(post("/signup")
                        .flashAttr("user", userDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("SignUp/signUpPage"))
                .andExpect(model().attribute("invalid", true));
    }
}
