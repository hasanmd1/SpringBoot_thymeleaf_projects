package com.example.libraryapplication.dto;

import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.dataModel.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    @NotEmpty(message = "Reservation date cannot be empty")
    private String reservationStatus;

    @NotEmpty(message = "Book copy cannot be empty")
    private BookCopy bookCopy;

    @NotEmpty(message = "User cannot be empty")
    private User user;

    @NotEmpty(message = "Reservation date cannot be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate reservationDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;

}
