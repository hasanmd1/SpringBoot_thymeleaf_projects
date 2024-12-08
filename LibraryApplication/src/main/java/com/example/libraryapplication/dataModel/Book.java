package com.example.libraryapplication.dataModel;

import jakarta.persistence.*;
import lombok.*;

import javax.annotation.processing.Generated;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String ISBN;

    private Date publicationDate;

    private String publisher;

    private Long numberOfCopies;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookAuthor> authors = new ArrayList<BookAuthor>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookGenre> genres = new ArrayList<BookGenre>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<BookCopy> bookCopies = new ArrayList<BookCopy>();
}
