package com.example.libraryapplication.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthorizationService {
    boolean authenticate(String username, String password);
    void logOut(HttpServletRequest request, HttpServletResponse response);

    boolean isAuthenticated();
}
