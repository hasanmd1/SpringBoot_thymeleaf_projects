package com.example.libraryapplication.service;

import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.PasswordResetDTO;
import com.example.libraryapplication.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public interface UserService {
    Optional<User> getUserById(Long id);

    List<User> getAllUsersByValue(String value);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    void activateAccount(User user);

    Optional<User> getUserByUsername(String username);

    User createUser(UserDTO userDTO);

    void resetPassword(User user, PasswordResetDTO passwordResetDTO);

    User updateUser(UserDTO userDTO, Long id);

    void deleteUser(Long id);
}
