package com.example.libraryapplication.dataModel;

import jakarta.persistence.*;
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
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String role;

    private String email;

    private String firstName;

    private String lastName;

    private String address;

    private String phoneNumber;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Reservation> reservations = new ArrayList<>();
}
