package com.techelevator.dao;

import com.techelevator.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate);


    List<Reservation> upcomingReservationsByPark(int site_id);

     List<Reservation> openUpcomingReservationsByPark(int park_id);

    List<Reservation> futureUpcomingReservationsByPark(int park_id, LocalDate startDate, LocalDate endDate);
}
