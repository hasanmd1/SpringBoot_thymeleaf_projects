package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByPhoneNumber(String phoneNumber);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getUsersByValue(String value);




    void saveUser(User user);
    void deleteUser(Long id);
}
