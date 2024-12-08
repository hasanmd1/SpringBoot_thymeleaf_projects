package com.example.libraryapplication.service.serviceImplementation;

import com.example.libraryapplication.dao.ReservationDAO;
import com.example.libraryapplication.dataModel.Reservation;
import com.example.libraryapplication.dataModel.User;
import com.example.libraryapplication.dto.ReservationDTO;
import com.example.libraryapplication.service.ReservationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO;

    public ReservationServiceImpl(ReservationDAO reservationDAO) {
        this.reservationDAO = reservationDAO;
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationDAO.getReservationById(id);
    }

    @Override
    public void createReservation(ReservationDTO reservationDTO, User user) {
        Reservation reservation = new Reservation();
        reservation.setCreationDate(reservationDTO.getReservationDate().atStartOfDay());
        reservation.setBookCopy(reservationDTO.getBookCopy());
        reservation.setDueDate(reservation.getCreationDate().plusDays(7));
        reservation.setReservationStatus(reservationDTO.getReservationStatus());
        if (Objects.equals(reservationDTO.getReservationStatus(), "RETURNED")) {
            reservation.setReturnDate(reservationDTO.getReturnDate() == null ? LocalDateTime.now() : reservationDTO.getReturnDate().atStartOfDay());
        }
        reservation.setFine(Objects.equals(reservationDTO.getReservationStatus(), "RETURNED") && reservation.getDueDate().isBefore(reservation.getReturnDate()) ? ChronoUnit.DAYS.between(reservation.getDueDate(), reservation.getReturnDate()) * 0.5 : 0.0);
        reservation.setReservationStatus(reservationDTO.getReservationStatus() != null ? reservationDTO.getReservationStatus() : "PENDING");
        reservation.setUser(user);
        reservationDAO.saveReservation(reservation);
    }

    @Override
    public void editReservation(Reservation reservation, ReservationDTO reservationDTO) {
        System.out.println(reservationDTO.getReservationDate() + " " + reservationDTO.getBookCopy() + " " + reservationDTO.getUser() + " " + reservationDTO.getReservationStatus() + " " + reservationDTO.getReturnDate());
        reservation.setBookCopy(reservationDTO.getBookCopy());
        reservation.setUser(reservationDTO.getUser());
        reservation.setCreationDate(reservationDTO.getReservationDate().atStartOfDay());
        reservation.setDueDate(reservation.getCreationDate().plusDays(7));
        reservation.setReservationStatus(reservationDTO.getReservationStatus());
        if (Objects.equals(reservationDTO.getReservationStatus(), "RETURNED")) {
            reservation.setReturnDate(reservationDTO.getReturnDate() == null ? LocalDateTime.now() : reservationDTO.getReturnDate().atStartOfDay());
        }
        reservation.setFine(Objects.equals(reservationDTO.getReservationStatus(), "RETURNED") && reservation.getDueDate().isBefore(reservation.getReturnDate()) ? ChronoUnit.DAYS.between(reservation.getDueDate(), reservation.getReturnDate()) * 0.5 : 0.0);
        reservationDAO.saveReservation(reservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationDAO.deleteReservationById(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDAO.getAllReservations();
    }
}
