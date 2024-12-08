package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.UserDAO;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.PasswordResetDTO;
import com.example.libraryapplication.dto.UserDTO;
import com.example.libraryapplication.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public List<User> getAllUsersByValue(String value) {
        return userDAO.getUsersByValue(value);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public void activateAccount(User user) {
        user.setRole("USER");
        userDAO.saveUser(user);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    @Override
    public User createUser(UserDTO userDTO) {
        User byEmail = userDAO.getUserByEmail(userDTO.getEmail()).orElse(null);
        User byPhoneNumber = userDAO.getUserByPhoneNumber(userDTO.getPhoneNumber()).orElse(null);
        User byUsername = userDAO.getUserByUsername(userDTO.getUsername()).orElse(null);
        if (byEmail != null || byPhoneNumber != null || byUsername != null) {
            return null;
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setRole("INACTIVE");

        userDAO.saveUser(user);

        return user;
    }

    @Override
    public void resetPassword(User user, PasswordResetDTO passwordResetDTO) {
        if (Objects.equals(passwordResetDTO.getNewPassword(), passwordResetDTO.getConfirmNewPassword())) {
            user.setPassword(passwordEncoder.encode(passwordResetDTO.getNewPassword()));
            userDAO.saveUser(user);
        }
    }

    @Override
    public User updateUser(UserDTO userDTO, Long id) {
        User updatedUser = userDAO.getUserById(id).orElse(null);
        if (updatedUser == null) {
            return null;
        }

        if (userDTO.getRole() != null) {
            updatedUser.setRole(userDTO.getRole());
        }

        if (userDTO.getAuthor() != null) {
            updatedUser.setAuthor(userDTO.getAuthor());
        }

        if (userDTO.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        updatedUser.setFirstName(userDTO.getFirstName());
        updatedUser.setLastName(userDTO.getLastName());
        updatedUser.setAddress(userDTO.getAddress());
        updatedUser.setPhoneNumber(userDTO.getPhoneNumber());

        userDAO.saveUser(updatedUser);

        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }

}
