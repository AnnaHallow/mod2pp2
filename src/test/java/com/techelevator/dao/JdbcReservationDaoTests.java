package com.techelevator.dao;

import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JdbcReservationDaoTests extends BaseDaoTests {

    private ReservationDao dao;

    @Before
    public void setup() {
        dao = new JdbcReservationDao(dataSource);
    }

    @Test
    public void createReservation_Should_ReturnNewReservationId() {
        int reservationCreated = dao.createReservation(1,
                "TEST NAME",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        assertEquals(5, reservationCreated);
    }

    @Test
    public void upcomingReservationByPark_Should_ReturnUpcomingReservation() {
        List<Reservation> reservations = dao.upcomingReservationsByPark(1);

        assertEquals(2, reservations.size());
        //assertEquals("Ohio", parks.get(0).getLocation());
        //assertEquals("Pennsylvania", parks.get(1).getLocation());
    }

    @Test
    public void openUpcomingReservationsByPark() {
        List<Reservation> reservations = dao.openUpcomingReservationsByPark(1);

        assertEquals(2, reservations.size());
        //assertEquals("Ohio", parks.get(0).getLocation());
        //assertEquals("Pennsylvania", parks.get(1).getLocation());
    }

    @Test
    public void futureUpcomingReservationsByPark() {
        List<Reservation> reservations = dao.futureUpcomingReservationsByPark(1,LocalDate.now().plusDays(3),LocalDate.now().plusDays(5) );

        assertEquals(2, reservations.size());
        //assertEquals("Ohio", parks.get(0).getLocation());
        //assertEquals("Pennsylvania", parks.get(1).getLocation());
    }
}
