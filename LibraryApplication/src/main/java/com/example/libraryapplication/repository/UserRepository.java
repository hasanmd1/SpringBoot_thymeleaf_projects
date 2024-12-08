package com.example.libraryapplication.repository;

import com.example.libraryapplication.dataModel.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findUsersByUsernameIgnoreCaseOrEmailIgnoreCaseOrFirstNameIgnoreCaseOrLastNameIgnoreCase(String value, String value1, String value2, String value3);
}
