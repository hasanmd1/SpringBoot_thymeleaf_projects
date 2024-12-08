package com.example.libraryapplication.serviceTest;

import com.example.libraryapplication.dao.ReservationDAO;
import com.example.libraryapplication.dataModel.BookCopy;
import com.example.libraryapplication.dataModel.Reservation;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.ReservationDTO;
import com.example.libraryapplication.service.serviceImplementation.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceImplTest {

    @Mock
    private ReservationDAO reservationDAO;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User user;
    private ReservationDTO reservationDTO;
    private BookCopy bookCopy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setting up sample user
        user = new User();
        user.setId(1L);
        user.setUsername("john.doe");

        // Setting up sample book copy
        bookCopy = new BookCopy();
        bookCopy.setId(1L);

        // Setting up sample reservation DTO
        reservationDTO = new ReservationDTO();
        reservationDTO.setReservationDate(LocalDate.now());
        reservationDTO.setBookCopy(bookCopy);
        reservationDTO.setReservationStatus("PENDING");
    }

    @Test
    void testCreateReservation() {
        
        reservationService.createReservation(reservationDTO, user);

        
        verify(reservationDAO, times(1)).saveReservation(any(Reservation.class));
    }

    @Test
    void testEditReservation() {
        
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);

        
        ReservationDTO updatedReservationDTO = new ReservationDTO();
        updatedReservationDTO.setReservationDate(LocalDate.now().plusDays(1));
        updatedReservationDTO.setBookCopy(bookCopy);
        updatedReservationDTO.setReservationStatus("RETURNED");
        updatedReservationDTO.setReturnDate(LocalDate.now().plusDays(10));

        
        reservationService.editReservation(reservation, updatedReservationDTO);

        
        assertEquals(updatedReservationDTO.getReservationDate().atStartOfDay(), reservation.getCreationDate());
        assertEquals("RETURNED", reservation.getReservationStatus());
        assertNotNull(reservation.getReturnDate());
        verify(reservationDAO, times(1)).saveReservation(reservation);
    }

    @Test
    void testDeleteReservation() {
        
        reservationService.deleteReservation(1L);

        
        verify(reservationDAO, times(1)).deleteReservationById(1L);
    }

    @Test
    void testGetReservationById() {
        
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setUser(user);
        when(reservationDAO.getReservationById(1L)).thenReturn(Optional.of(reservation));

        
        Optional<Reservation> result = reservationService.getReservationById(1L);

        
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetAllReservations() {
        
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        reservation1.setUser(user);
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setUser(user);

        when(reservationDAO.getAllReservations()).thenReturn(List.of(reservation1, reservation2));

        
        List<Reservation> reservations = reservationService.getAllReservations();

        
        assertNotNull(reservations);
        assertEquals(2, reservations.size());
        assertEquals(1L, reservations.get(0).getId());
        assertEquals(2L, reservations.get(1).getId());
    }
}
