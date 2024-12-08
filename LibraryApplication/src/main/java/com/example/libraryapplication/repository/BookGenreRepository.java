package com.example.libraryapplication.repository;

import com.example.libraryapplication.dataModel.BookGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGenreRepository extends JpaRepository<BookGenre, Long> {

}
