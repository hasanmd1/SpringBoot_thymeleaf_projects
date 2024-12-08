package com.example.libraryapplication.controllers;

import com.example.libraryapplication.dataModel.*;
import com.example.libraryapplication.dto.BookDTO;
import com.example.libraryapplication.service.AuthorService;
import com.example.libraryapplication.service.BookService;
import com.example.libraryapplication.service.GenreService;
import com.example.libraryapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @GetMapping("/user/{userId}/book")
    public String getBook(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("books", bookService.getAllBooks());
        return "Book/displayBook";
    }

    @GetMapping("/user/{userId}/book/createBook")
    public String getCreateBook(@PathVariable("userId") Long userId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            model.addAttribute("books", bookService.getAllBooks());
            return "Book/displayBook";
        }
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("book", new BookDTO());
        model.addAttribute("genres", genreService.getAllGenres());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "Book/createBook";
    }

    @PostMapping("/user/{userId}/book/createBook")
    public String createBook(@PathVariable("userId") Long userId, @ModelAttribute("book") BookDTO bookDTO, BindingResult bindingResult, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            model.addAttribute("books", bookService.getAllBooks());
            return "Book/displayBook";
        }
        if (bindingResult.hasErrors() || bookDTO.getNumberOfCopies() < 0) {
            model.addAttribute("book", bookDTO);
            model.addAttribute("genres", genreService.getAllGenres());
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("invalid", true);
            return "Book/createBook";
        }
        bookService.createBook(bookDTO);
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("books", bookService.getAllBooks());
        return "Book/displayBook";
    }

    @GetMapping("/user/{userId}/book/{bookId}/editBook")
    public String getEditBook(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Book> book = bookService.getBookById(bookId);

        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            model.addAttribute("books", bookService.getAllBooks());
            return "Book/displayBook";
        }

        Book b = book.orElseGet(Book::new);
        BookDTO bookDTO = new BookDTO(
                b.getTitle(),
                b.getISBN(),
                b.getPublicationDate(),
                b.getPublisher(),
                b.getNumberOfCopies(),
                b.getGenres().stream().map(g -> g.getGenre().getId()).toList(),
                b.getAuthors().stream().map(a -> a.getAuthor().getId()).toList()
        );
        List<Genre> genres = genreService.getAllGenres();
        List<Author> authors = authorService.getAllAuthors();
        Map<Long, String> genreMap = genres.stream().collect(Collectors.toMap(Genre::getId, Genre::getName));
        Map<Long, String> authorMap = authors.stream().collect(Collectors.toMap(Author::getId, author -> author.getFirstName()+" "+ author.getLastName()));


        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("bookDTO", bookDTO);
        model.addAttribute("book", book.orElseGet(Book::new));
        model.addAttribute("genres", genres);
        model.addAttribute("authors", authors);
        model.addAttribute("authorMap", authorMap);
        model.addAttribute("genreMap", genreMap);
        return "Book/editBook";
    }

    @PostMapping("/user/{userId}/book/{bookId}/editBook")
    public String editBook(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId, @ModelAttribute("bookDTO") BookDTO bookDTO, BindingResult bindingResult, Model model) {
        Optional<User> user = userService.getUserById(userId);
        Optional<Book> book = bookService.getBookById(bookId);
        if (user.isEmpty() || (!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            model.addAttribute("books", bookService.getAllBooks());
            return "Book/displayBook";
        }

        if (bindingResult.hasErrors() || book.isEmpty()) {
            List<Genre> genres = genreService.getAllGenres();
            List<Author> authors = authorService.getAllAuthors();
            Map<Long, String> genreMap = genres.stream().collect(Collectors.toMap(Genre::getId, Genre::getName));
            Map<Long, String> authorMap = authors.stream().collect(Collectors.toMap(Author::getId, author -> author.getFirstName()+" "+ author.getLastName()));


            model.addAttribute("authors", authors);
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("bookDTO", bookDTO);
            model.addAttribute("book", book.orElseGet(Book::new));
            model.addAttribute("genres", genres);
            model.addAttribute("invalid", true);
            model.addAttribute("authorMap", authorMap);
            model.addAttribute("genreMap", genreMap);
            return "Book/editBook";
        }
        bookService.editBook(bookId, bookDTO);
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("books", bookService.getAllBooks());
        return "Book/displayBook";

    }

    @GetMapping("/user/{userId}/book/{bookId}/deleteBook")
    public String deleteBook(@PathVariable("userId") Long userId, @PathVariable("bookId") Long bookId, Model model) {
        Optional<User> user = userService.getUserById(userId);
        if (user.isPresent() && (user.get().getRole().equals("ADMIN") || user.get().getRole().equals("LIBRARIAN"))) {
            bookService.deleteBook(bookId);
        }
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("books", bookService.getAllBooks());
        return "Book/displayBook";
    }
}
