package com.example.libraryapplication.controllerTest;

import com.example.libraryapplication.controllers.ReservationController;
import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.dataModel.Reservation;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.ReservationDTO;
import com.example.libraryapplication.service.BookCopyService;
import com.example.libraryapplication.service.ReservationService;
import com.example.libraryapplication.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReservationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserService userService;

    @Mock
    private BookCopyService bookCopyService;

    @InjectMocks
    private ReservationController reservationController;

    private User mockUser;
    private Reservation mockReservation;
    private BookCopy mockBookCopy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");
        mockUser.setRole("USER");

        mockBookCopy = new BookCopy();
        mockBookCopy.setId(1L);
        mockBookCopy.setAvailable(true);

        mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setUser(mockUser);
        mockReservation.setBookCopy(mockBookCopy);
    }

    @Test
    void testGetReservationPage() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(reservationService.getAllReservations()).thenReturn(List.of(mockReservation));

        
        mockMvc.perform(get("/user/1/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("Reservation/displayReservationPage"))
                .andExpect(model().attributeExists("user", "reservations"))
                .andExpect(model().attribute("user", mockUser))
                .andExpect(model().attribute("reservations", List.of(mockReservation)));

        verify(userService, times(1)).getUserById(1L);
        verify(reservationService, times(1)).getAllReservations();
    }

    @Test
    void testGetCreateReservationPage() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(bookCopyService.getAllBookCopies()).thenReturn(List.of(mockBookCopy));
        when(userService.getAllUsers()).thenReturn(List.of(mockUser));

        
        mockMvc.perform(get("/user/1/reservation/createReservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("Reservation/createReservationPage"))
                .andExpect(model().attributeExists("users", "user", "bookCopies", "reservation"))
                .andExpect(model().attribute("bookCopies", List.of(mockBookCopy)));

        verify(bookCopyService, times(1)).getAllBookCopies();
        verify(userService, times(2)).getUserById(1L);
    }

    @Test
    void testCreateReservationValid() throws Exception {
        
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationStatus("ACTIVE");
        reservationDTO.setUser(mockUser);
        reservationDTO.setBookCopy(mockBookCopy);

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(post("/user/1/reservation/createReservation")
                        .flashAttr("reservationDTO", reservationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Reservation/displayReservationPage"))
                .andExpect(model().attributeExists("user", "reservations"));

        verify(reservationService, times(1)).createReservation(reservationDTO, mockUser);
    }

    @Test
    void testGetEditReservationPage() throws Exception {
        mockReservation.setCreationDate(LocalDateTime.now()); 

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(mockReservation));
        when(bookCopyService.getAllBookCopies()).thenReturn(List.of(mockBookCopy));
        when(userService.getAllUsers()).thenReturn(List.of(mockUser));

        
        mockMvc.perform(get("/user/1/reservation/1/editReservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("Reservation/editReservationPage"))
                .andExpect(model().attributeExists("reservation", "reservationDTO", "users", "bookCopies"));

        verify(reservationService, times(1)).getReservationById(1L);
    }


    @Test
    void testEditReservationValid() throws Exception {
        
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setReservationStatus("UPDATED");

        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));
        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(mockReservation));

        
        mockMvc.perform(post("/user/1/reservation/1/editReservation")
                        .flashAttr("reservationDTO", reservationDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("Reservation/displayReservationPage"));

        verify(reservationService, times(1)).editReservation(mockReservation, reservationDTO);
    }

    @Test
    void testDeleteReservation() throws Exception {
        
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        
        mockMvc.perform(get("/user/1/reservation/1/deleteReservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("Reservation/displayReservationPage"));

        verify(reservationService, times(1)).deleteReservation(1L);
    }
}
