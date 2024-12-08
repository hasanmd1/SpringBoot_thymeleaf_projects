package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.UserDAO;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationServiceImpl implements AuthorizationService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServiceImpl(UserDAO userDao, PasswordEncoder passwordEncoder) {
        this.userDAO = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean authenticate(String username, String password) {
        User user = userDAO.getUserByUsername(username).orElse(null);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            System.out.println("Logging out user: " + authentication.getName());
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"));
    }
}
