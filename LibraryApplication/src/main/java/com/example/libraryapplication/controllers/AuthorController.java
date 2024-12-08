package com.example.libraryapplication.controllers;


import com.example.libraryapplication.dataModel.Author;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.AuthorDTO;
import com.example.libraryapplication.service.AuthorService;
import com.example.libraryapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;
    private final UserService userService;

    @GetMapping("/user/{userId}/author")
    public String getAuthor(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        return "Author/displayAuthor";
    }

    @GetMapping("/user/{userId}/author/createAuthor")
    public String getCreateAuthor(@PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("user", user.orElseGet(User::new));
            return "Author/displayAuthor";
        }

        model.addAttribute("author", new AuthorDTO());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user.orElseGet(User::new));
        return "Author/createAuthor";
    }

    @PostMapping("/user/{userId}/author/createAuthor")
    public String createAuthor(@PathVariable("userId") Long userId, @ModelAttribute("author") AuthorDTO authorDTO, Model model, BindingResult bindingResult) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("user", user.orElseGet(User::new));
            return "Author/displayAuthor";
        }
        if (bindingResult.hasErrors()) {
            model.addAttribute("author", authorDTO);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("invalid", true);
            return "Author/createAuthor";
        }
        authorService.createAuthor(authorDTO);
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        return "Author/displayAuthor";
    }

    @GetMapping("/user/{userId}/author/{authorId}/editAuthor")
    public String getEditAuthor(@PathVariable("authorId") Long authorId, @PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            return "Author/displayAuthor";
        }
        Author author = authorService.getAuthorById(authorId).orElseGet(Author::new);
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setFirstName(author.getFirstName());
        authorDTO.setLastName(author.getLastName());
        authorDTO.setUser(author.getUser());

        model.addAttribute("author", author);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("authorDTO", authorDTO);
        return "Author/editAuthor";
    }

    @PostMapping("/user/{userId}/author/{authorId}/editAuthor")
    public String editAuthor(@PathVariable("authorId") Long authorId, @PathVariable("userId") Long userId, AuthorDTO authorDTO, Model model, BindingResult bindingResult) {
        Optional<User> user = userService.getUserById(userId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("author", authorDTO);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("invalid", true);
            return "Author/editAuthor";
        }

        if (user.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            authorService.editAuthor(authorId, authorDTO);
        }
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        return "Author/displayAuthor";

    }

    @GetMapping("/user/{userId}/author/{authorId}/deleteAuthor")
    public String deleteAuthor(@PathVariable("authorId") Long authorId, @PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            authorService.deleteAuthor(authorId);
        }
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        return "Author/displayAuthor";
    }
}
