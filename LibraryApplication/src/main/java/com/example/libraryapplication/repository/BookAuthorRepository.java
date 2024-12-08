package com.example.libraryapplication.repository;

import com.example.libraryapplication.dataModel.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {

    List<BookAuthor> findByBookId(Long bookId);

    List<BookAuthor> findByAuthorId(Long authorId);

    List<BookAuthor> findByBookIdAndAuthorId(Long bookId, Long authorId);

}
