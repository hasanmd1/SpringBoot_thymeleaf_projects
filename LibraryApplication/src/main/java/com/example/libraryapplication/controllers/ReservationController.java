package com.example.libraryapplication.controllers;

import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.dataModel.Reservation;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.ReservationDTO;
import com.example.libraryapplication.service.BookCopyService;
import com.example.libraryapplication.service.ReservationService;
import com.example.libraryapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final BookCopyService bookCopyService;

    @GetMapping("/user/{userId}/reservation")
    public String getReservation(@PathVariable("userId") Long userId, Model model) {
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "Reservation/displayReservationPage";
    }

    @GetMapping("/user/{userId}/reservation/createReservation")
    public String getCreateReservation(@PathVariable("userId") Long userId, Model model) {
        List<BookCopy> bookCopies = bookCopyService.getAllBookCopies().stream().filter(BookCopy::isAvailable).toList();
        Optional<User> user = userService.getUserById(userId);
        List<User> users = userService.getAllUsers();
        if ((!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            users = List.of(user.orElseGet(User::new));
        }

        model.addAttribute("users", users);
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("bookCopies", bookCopies);
        model.addAttribute("reservation", new ReservationDTO());
        return "Reservation/createReservationPage";
    }

    @PostMapping("/user/{userId}/reservation/createReservation")
    public String createReservation(@PathVariable("userId") Long userId, ReservationDTO reservationDTO, BindingResult bindingResult, Model model) {
        System.out.println(reservationDTO.getReservationDate() + " " + reservationDTO.getBookCopy() + " " + reservationDTO.getUser() + " " + reservationDTO.getReservationStatus() + " " + reservationDTO.getReturnDate());
        Optional<User> user = userService.getUserById(userId);

        if (bindingResult.hasErrors() && user.isEmpty()) {
            List<BookCopy> bookCopies = bookCopyService.getAllBookCopies().stream().filter(BookCopy::isAvailable).toList();
            List<User> users = userService.getAllUsers();
            if ((!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
                users = List.of(user.orElseGet(User::new));
            }

            model.addAttribute("users", users);
            model.addAttribute("user", user.orElseGet(User::new));
            model.addAttribute("reservation", reservationDTO);
            model.addAttribute("invalid", true);
            model.addAttribute("bookCopies", bookCopies);
            return "Reservation/createReservationPage";
        }

        reservationService.createReservation(reservationDTO, user.orElseGet(User::new));

        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "Reservation/displayReservationPage";
    }

    @GetMapping("/user/{userId}/reservation/{reservationId}/editReservation")
    public String getEditReservation(@PathVariable("userId") Long userId, @PathVariable("reservationId") Long reservationId, Model model) {

        Optional<User> user = userService.getUserById(userId);
        Reservation reservation = reservationService.getReservationById(reservationId).orElseGet(Reservation::new);

        ReservationDTO reservationDTO = new ReservationDTO();

        if (user.isPresent()) {
            reservationDTO.setReservationDate(LocalDate.from(reservation.getCreationDate()));
            reservationDTO.setBookCopy(reservation.getBookCopy());
            reservationDTO.setUser(reservation.getUser());
            reservationDTO.setReservationStatus(reservation.getReservationStatus());
            reservationDTO.setReturnDate(reservation.getReturnDate() == null ? null : LocalDate.from(reservation.getReturnDate()));
        }

        List<User> users = userService.getAllUsers();
        if ((!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            users = List.of(user.orElseGet(User::new));
        }
        List<BookCopy> bookCopies = bookCopyService.getAllBookCopies().stream().filter(BookCopy::isAvailable).toList();

        model.addAttribute("bookCopies", bookCopies);
        model.addAttribute("users", users);
        model.addAttribute("reservation", reservation);
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("reservationDTO", reservationDTO);

        return "Reservation/editReservationPage";
    }

    @PostMapping("/user/{userId}/reservation/{reservationId}/editReservation")
    public String editReservation(@PathVariable("userId") Long userId, @PathVariable("reservationId") Long reservationId, Model model, ReservationDTO reservationDTO, BindingResult bindingResult) {
        Optional<User> user = userService.getUserById(userId);
        Reservation reservation = reservationService.getReservationById(reservationId).orElseGet(Reservation::new);

        if (!bindingResult.hasErrors() && user.isPresent()) {
            reservationService.editReservation(reservation, reservationDTO);
            model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
            model.addAttribute("reservations", reservationService.getAllReservations());
            return "Reservation/displayReservationPage";
        }
        List<User> users = userService.getAllUsers();
        if ((!user.get().getRole().equals("ADMIN") && !user.get().getRole().equals("LIBRARIAN"))) {
            users = List.of(user.orElseGet(User::new));
        }
        List<BookCopy> bookCopies = bookCopyService.getAllBookCopies().stream().filter(BookCopy::isAvailable).toList();

        model.addAttribute("users", users);
        model.addAttribute("user", user.orElseGet(User::new));
        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("bookCopies", bookCopies);
        model.addAttribute("reservation", reservation);
        model.addAttribute("invalid", true);
        return "Reservation/editReservationPage";

    }

    @GetMapping("/user/{userId}/reservation/{reservationId}/deleteReservation")
    public String deleteReservation(@PathVariable("userId") Long userId, @PathVariable("reservationId") Long reservationId, Model model) {
        reservationService.deleteReservation(reservationId);
        model.addAttribute("user", userService.getUserById(userId).orElseGet(User::new));
        model.addAttribute("reservations", reservationService.getAllReservations());
        return "Reservation/displayReservationPage";
    }
}
