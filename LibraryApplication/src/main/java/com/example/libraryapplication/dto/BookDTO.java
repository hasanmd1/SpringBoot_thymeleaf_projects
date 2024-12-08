package com.example.libraryapplication.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "ISBN cannot be empty")
    private String ISBN;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publicationDate;
    private String publisher;

    @NotEmpty(message = "Number of copies cannot be empty")
    private Long numberOfCopies;
    private List<Long> bookAuthorIds = new ArrayList<>();
    private List<Long> genreIds = new ArrayList<>();
}
