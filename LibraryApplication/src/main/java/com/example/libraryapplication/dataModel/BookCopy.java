package com.example.libraryapplication.dataModel;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private boolean available;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Book book;

    @OneToMany(mappedBy = "bookCopy", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    public Boolean isAvailable() {
        return available;
    }
}
