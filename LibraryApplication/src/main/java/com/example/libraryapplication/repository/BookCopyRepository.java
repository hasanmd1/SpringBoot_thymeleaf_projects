package com.example.libraryapplication.repository;

import com.example.libraryapplication.dataModel.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    List<BookCopy> findByBookId(Long bookId);
    List<BookCopy> findByBookIdAndAvailableTrue(Long bookId);
    List<BookCopy> findByBookIdAndAvailableFalse(Long bookId);
}
