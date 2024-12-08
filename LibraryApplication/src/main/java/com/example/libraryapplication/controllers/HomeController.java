package com.example.libraryapplication.controllers;

import com.example.libraryapplication.configuration.CustomUserDetails;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.service.AuthorizationService;
import com.example.libraryapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final AuthorizationService authorizationService;
    private final UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("Authentication object: " + authentication.isAuthenticated());
        System.out.println("Principal: " + authentication.getPrincipal());

        boolean isAuthenticated = authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));

        model.addAttribute("authenticated", isAuthenticated);

        if (isAuthenticated) {
            System.out.println("Authenticated user: " + authentication.getPrincipal());
            System.out.println(isAuthenticated);
            String username = (String) authentication.getPrincipal();
            model.addAttribute("user", userService.getUserByUsername(username).orElseGet(User::new));
            model.addAttribute("login", true);
            return "Home";
        } else {
            model.addAttribute("user", new User());
            model.addAttribute("login", false);
            return "Home";
        }
    }

    @GetMapping("/home")
    public String getHome() {
        return "redirect:/";
    }


}
