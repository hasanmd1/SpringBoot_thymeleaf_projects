package com.example.libraryapplication.controllers;

import com.example.libraryapplication.dataModel.Genre;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.GenreDTO;
import com.example.libraryapplication.service.GenreService;
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
public class GenreController {

    private final GenreService genreService;
    private final UserService userService;

    @GetMapping("/user/{userId}/bookGenre")
    public String getBookGenre(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("genres", genreService.getAllGenres());
        return "BookGenre/displayBookGenre";
    }

    @GetMapping("/user/{userId}/bookGenre/createBookGenre")
    public String getCreateBookGenre(@PathVariable("userId") Long userId, Model model) {
        System.out.println("Executing getCreateBookGenre");
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("genres", genreService.getAllGenres());
            return "BookGenre/displayBookGenre";
        }
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("genre", new GenreDTO());
        return "BookGenre/createBookGenre";
    }

    @PostMapping("/user/{userId}/bookGenre/createBookGenre")
    public String createBookGenre(@PathVariable("userId") Long userId, Model model, @ModelAttribute("genre") GenreDTO genreDTO, BindingResult bindingResult) {
        Optional<User> user = userService.getUserById(userId);
        if (bindingResult.hasErrors() || user.isEmpty()) {
            model.addAttribute("genre", genreDTO);
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("invalid", true);
            return "BookGenre/createBookGenre";
        }
        if (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN")) {
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("genres", genreService.getAllGenres());
            return "BookGenre/displayBookGenre";
        }
        genreService.createGenre(genreDTO);
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("genres", genreService.getAllGenres());
        return "BookGenre/displayBookGenre";
    }

    @GetMapping("/user/{userId}/bookGenre/{genreId}/editBookGenre")
    public String getEditBookGenre(@PathVariable("userId") Long userId, @PathVariable("genreId") Long genreId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Genre> genre = genreService.getGenreById(genreId);
        if (user.isPresent() && genre.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("genre", genre.orElseGet(Genre::new));
            model.addAttribute("genreDTO", new GenreDTO(genre.get().getName()));
            return "BookGenre/editBookGenre";
        }
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("genres", genreService.getAllGenres());
        return "BookGenre/displayBookGenre";
    }

    @PostMapping("/user/{userId}/bookGenre/{genreId}/editBookGenre")
    public String editBookGenre(@PathVariable("userId") Long userId, @PathVariable("genreId") Long genreId, @ModelAttribute("genreDTO") GenreDTO genreDTO, Model model) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Genre> genre = genreService.getGenreById(genreId);
        if (user.isPresent() && genre.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            genreService.editGenre(genreId, genreDTO);
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("genres", genreService.getAllGenres());
            return "BookGenre/displayBookGenre";
        }
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("genre", genreDTO);
        model.addAttribute("invalid", true);
        return "BookGenre/editBookGenre";
    }

    @GetMapping("/user/{userId}/bookGenre/{genreId}/deleteBookGenre")
    public String deleteBookGenre(@PathVariable("userId") Long userId, @PathVariable("genreId") Long genreId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            genreService.deleteGenre(genreId);
        }
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("genres", genreService.getAllGenres());
        return "BookGenre/displayBookGenre";
    }
}
