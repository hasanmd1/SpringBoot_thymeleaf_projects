package com.example.libraryapplication.dataModel;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<BookAuthor> bookAuthors = new ArrayList<>();

    @OneToOne(mappedBy = "author", cascade = CascadeType.PERSIST)
    private User user;

}
