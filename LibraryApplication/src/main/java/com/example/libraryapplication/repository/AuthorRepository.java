package com.example.libraryapplication.repository;

import com.example.libraryapplication.dataModel.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByLastName(String name);
    List<Author> findByFirstName(String name);
    List<Author> findByLastNameAndFirstName(String lastName, String firstName);
}
