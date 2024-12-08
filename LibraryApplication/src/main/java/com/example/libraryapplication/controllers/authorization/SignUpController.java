package com.example.libraryapplication.controllers.authorization;


import com.example.libraryapplication.configuration.EmailSender;
import com.example.libraryapplication.dataModel.TokenInfo;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    private final EmailSender emailService;

    private final Map<String, TokenInfo> tokenStore = new HashMap<>();

    @GetMapping("/signup")
    public String getSignUp(Model model) {
        model.addAttribute("user", new UserDTO());
        return "SignUp/signUpPage";
    }

    @PostMapping("/signup")
    public String signUp(Model model, @ModelAttribute("user") UserDTO userDTO, HttpServletRequest request) throws MessagingException {
        System.out.println("Sign Up process started....");
        if ((userService.getUserByUsername(userDTO.getUsername()).isPresent() || userService.getUserByEmail(userDTO.getEmail()).isPresent() || userService.getUserByEmail(userDTO.getEmail()).isPresent())) {
            model.addAttribute("user", userDTO);
            model.addAttribute("invalid", true);
            return "SignUp/signUpPage";
        }
        System.out.println(userDTO.getFirstName() + " " + userDTO.getLastName() + " " + userDTO.getUsername() + " " + userDTO.getPassword() + " " + userDTO.getEmail() + " " + userDTO.getAddress() + " " + userDTO.getPhoneNumber());

        User newUser = userService.createUser(userDTO);

        if (newUser == null) {
            model.addAttribute("user", userDTO);
            model.addAttribute("invalid", true);
            return "SignUp/signUpPage";
        }


        //generate token and send email
        String token = UUID.randomUUID().toString();
        tokenStore.put(newUser.getEmail(), new TokenInfo(token, LocalDateTime.now().plusHours(24)));

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String verificationUrl = baseUrl + "/signup/emailVerification?token=" + token;

        emailService.sendVerification(newUser.getEmail(), verificationUrl);
        model.addAttribute("user", newUser);
        return "SignUp/emailVerification";
    }

    @GetMapping("/signup/emailVerification")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        Optional<Map.Entry<String, TokenInfo>> entry = tokenStore.entrySet().stream()
                .filter(e -> e.getValue().getToken().equals(token))
                .findFirst();

        String email = "";

        if (entry.isPresent()) {
            email = entry.get().getKey();
            TokenInfo tokenInfo = entry.get().getValue();

            if (tokenInfo.getExpiryTime().isBefore(LocalDateTime.now())) {
                model.addAttribute("invalid", true);
                model.addAttribute("user", new UserDTO());
                return "SignUp/signUpPage";
            }
        }
        Optional<User> user = userService.getUserByEmail(email);
        tokenStore.remove(email);
        userService.activateAccount(user.orElseGet(User::new));
        return "redirect:/login";
    }

    @GetMapping("/signup/resendVerification")
    public String resendVerification(@RequestParam("email") String email, Model model, HttpServletRequest request) throws MessagingException {
        String token = tokenStore.get(email).getToken();

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        String verificationUrl = baseUrl + "/signup/emailVerification?token=" + token;

        emailService.sendVerification(email, verificationUrl);
        model.addAttribute("user", userService.getUserByEmail(email).orElseGet(User::new));
        return "SignUp/emailVerification";
    }
}
