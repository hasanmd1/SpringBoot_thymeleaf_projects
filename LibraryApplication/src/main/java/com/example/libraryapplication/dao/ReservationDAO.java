package com.example.libraryapplication.dao;

import com.example.libraryapplication.dataModel.Reservation;

import java.util.List;
import java.util.Optional;


public interface ReservationDAO {
    Optional<Reservation> getReservationById(Long id);
    List<Reservation> getAllReservations();
    List<Reservation> getReservationsByUserId(Long id);
    List<Reservation> getReservationsByBookCopyId(Long id);



    void saveReservation(Reservation reservation);
    void deleteReservationById(Long id);

}
