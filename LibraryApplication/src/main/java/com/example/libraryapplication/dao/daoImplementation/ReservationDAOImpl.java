package com.example.libraryapplication.dao.daoImplementation;

import com.example.libraryapplication.dao.ReservationDAO;
import com.example.libraryapplication.dataModel.Reservation;
import com.example.libraryapplication.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationDAOImpl implements ReservationDAO {

    private final ReservationRepository reservationRepository;

    public ReservationDAOImpl(ReservationRepository reservationReepository) {
        this.reservationRepository = reservationReepository;
    }

    @Override
    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Override
    public List<Reservation> getReservationsByUserId(Long id) {
        return reservationRepository.findByUserId(id);
    }

    @Override
    public List<Reservation> getReservationsByBookCopyId(Long id) {
        return reservationRepository.findByBookCopyId(id);
    }

    @Override
    public void saveReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Override
    public void deleteReservationById(Long id) {
        reservationRepository.deleteById(id);
    }
}
