package com.example.libraryapplication.controllers;

import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final GenreService genreService;
    private final ReservationService reservationService;
    private final BookService bookService;
    private final AuthorService authorService;

    @GetMapping("/user/{userId}")
    public String getUser(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("genre", genreService.getAllGenres());
        model.addAttribute("author", authorService.getAllAuthors());
        model.addAttribute("books", bookService.getAllBooks());
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "User/displayUser";
    }

    @GetMapping("/user/{userId}/editUser")
    public String getEditUser(@PathVariable("userId") Long userId, Model model) {
        User user = userService.getUserById(userId).orElseGet(User::new);
        UserDTO userDTO = new UserDTO(user.getUsername(), user.getPassword(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getAddress(), user.getPhoneNumber(), user.getRole(), user.getAuthor());
        model.addAttribute("user", user);
        model.addAttribute("userDTO", userDTO);
        return "User/editUser";
    }

    @PostMapping("/user/{userId}/editUser")
    public String editUser(@PathVariable("userId") Long userId, @ModelAttribute("userDTO") UserDTO userDTO, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent()) {
            User updatedUser = userService.updateUser(userDTO, userId);
            model.addAttribute("genre", genreService.getAllGenres());
            model.addAttribute("user", updatedUser);
            model.addAttribute("author", authorService.getAllAuthors());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "User/displayUser";
        } else {
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("invalid", true);
            return "User/editUser";
        }
    }

    @GetMapping("/user/{userId}/searchUser")
    public String searchUser(@PathVariable("userId") Long userId, @RequestParam(value = "search", required = false) String search, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (Objects.equals(user.get().getRole(), "USER"))) {
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("genre", genreService.getAllGenres());
            model.addAttribute("author", authorService.getAllAuthors());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "User/displayUser";
        }
        if (search == null) {
            search = "";
        } else {
            search = search.toLowerCase();
        }
        List<User> userList = userService.getAllUsersByValue(search.trim());


        model.addAttribute("userList", userList);
        model.addAttribute("user", user.orElseGet(User::new));
        return "User/searchUser";

    }

    @GetMapping("/user/{userId}/deleteUser")
    public String getDeleteUser(@PathVariable("userId") Long userId, User currentUser, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            userService.deleteUser(userId);
        }
        if (Objects.equals(userId, currentUser.getId())) {
            return "redirect:/logout";
        } else {
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            model.addAttribute("genre", genreService.getAllGenres());
            model.addAttribute("author", authorService.getAllAuthors());
            model.addAttribute("books", bookService.getAllBooks());
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "User/displayUser";
        }
    }

}
