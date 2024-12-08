package com.example.libraryapplication.dataModel;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationDate;

    private LocalDateTime dueDate;

    private LocalDateTime returnDate;

    private Double fine;

    private String reservationStatus;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private BookCopy bookCopy;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private User user;
}
