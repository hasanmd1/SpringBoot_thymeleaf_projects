package com.example.libraryapplication.controllers.authorization;

import com.example.libraryapplication.configuration.EmailSender;
import com.example.libraryapplication.dataModel.TokenInfo;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.PasswordResetDTO;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.AuthorizationService;
import com.example.libraryapplication.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class LogInController {

    private final AuthorizationService authorizationService;
    private final UserService userService;
    private final EmailSender emailSender;
    private final Map<String, TokenInfo> tokenStore = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String getLogIn(Model model) {
        return "LogIn/logInPage";
    }

    @GetMapping("/forgotPassword")
    public String getForgotPassword(Model model) {
        model.addAttribute("user", new UserDTO());
        return "LogIn/forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(UserDTO userDTO, BindingResult result, Model model, HttpServletRequest request) throws MessagingException {
        Optional<User> user = userService.getUserByEmail(userDTO.getEmail());
        if (result.hasErrors() || user.isEmpty() || !user.get().getEmail().equals(userDTO.getEmail())) {
            model.addAttribute("user", userDTO);
            model.addAttribute("invalid", true);
            return "LogIn/forgotPassword";
        }

        tokenStore.remove(userDTO.getEmail());
        String token = UUID.randomUUID().toString();
        tokenStore.put(userDTO.getEmail(), new TokenInfo(token, LocalDateTime.now().plusMinutes(10)));

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String forgotPasswordUrl = baseUrl + "/forgotPassword/redirect?token=" + token;
        emailSender.sendPasswordResetUrl(userDTO.getEmail(), forgotPasswordUrl);

        model.addAttribute("user", userDTO);
        return "LogIn/linkSent";
    }

    @GetMapping("/forgotPassword/resend/")
    public String resendPasswordUrl (@RequestParam("email") String email, Model model, HttpServletRequest request, UserDTO userDTO) throws MessagingException {
        if (tokenStore.containsKey(email)) {
            TokenInfo tokenInfo = tokenStore.get(email);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            String forgotPasswordUrl = baseUrl + "/forgotPassword/redirect?token=" + tokenInfo.getToken();
            emailSender.sendPasswordResetUrl(userDTO.getEmail(), forgotPasswordUrl);
        }
        model.addAttribute("user", userDTO);
        return "LogIn/linkSent";
    }

    @GetMapping("/forgotPassword/redirect")
    public String redirectToResetPassword(@RequestParam("token") String token, Model model, PasswordResetDTO passwordResetDTO){

        Optional<Map.Entry<String, TokenInfo>> entry = tokenStore.entrySet().stream()
                .filter(e -> e.getValue().getToken().equals(token))
                .findFirst();

        if (entry.isPresent()) {
            String email = entry.get().getKey();
            TokenInfo tokenInfo = entry.get().getValue();
            if (tokenInfo.getExpiryTime().isAfter(LocalDateTime.now())) {
                model.addAttribute("passwordBox", passwordResetDTO);
                model.addAttribute("token", token);
                System.out.println("resetPassword:::" + email);
                model.addAttribute("user", userService.getUserByEmail(email).orElse(null));
                return "LogIn/resetPassword";
            }
        }

        model.addAttribute("invalidToken", true);
        model.addAttribute("user", new UserDTO());
        return "LogIn/forgotPassword";
    }

    @GetMapping("/resetPassword")
    public String getResetPassword(@RequestParam("userId") Long userId, Model model){
        User user = userService.getUserById(userId).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("passwordBox", new PasswordResetDTO());

        return "LogIn/resetPassword";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam("userId") Long userId, PasswordResetDTO passwordResetDTO, BindingResult result, Model model){

        User user = userService.getUserById(userId).orElse(null);
        if (result.hasErrors() || user == null || !passwordResetDTO.getNewPassword().equals(passwordResetDTO.getConfirmNewPassword())) {
            model.addAttribute("user", user);
            model.addAttribute("passwordBox", passwordResetDTO);
            model.addAttribute("invalid", true);
            return "LogIn/resetPassword";
        }
        userService.resetPassword(user, passwordResetDTO);

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, HttpServletResponse response, Model model) {
        authorizationService.logOut(request, response);
        model.addAttribute("logout", true);
        return "Home";
    }
}
