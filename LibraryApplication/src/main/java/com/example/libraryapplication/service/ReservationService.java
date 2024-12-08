package com.example.libraryapplication.service;

import com.example.libraryapplication.dataModel.Reservation;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.ReservationDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ReservationService {
    Optional<Reservation> getReservationById(Long id);

    void createReservation(ReservationDTO reservationDTO, User user);

    void editReservation(Reservation reservation, ReservationDTO reservationDTO);

    void deleteReservation(Long id);

    List<Reservation> getAllReservations();
}
