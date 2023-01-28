package com.techelevator.dao;

import com.techelevator.model.Campground;
import com.techelevator.model.Reservation;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        // Book a reservation at a selected campsite

        String sql = "INSERT INTO reservation(site_id, name, from_date, to_date) " +
                "VALUES (? , ? , ? , ? ) " +
                "RETURNING reservation_id ";
        Integer results = jdbcTemplate.queryForObject(sql,int.class, siteId, name, fromDate, toDate);

        return results;
    }

    public List<Reservation> upcomingReservationsByPark(int parkId){
        //View list of upcoming reservations in the next 30 days of selected park
        //Include reservation id, site id, name, state date, end date, and date created.
        List<Reservation> reservations = new ArrayList<>();

        String sql = "SELECT r.reservation_id, r.site_id, r.name, r.from_date, r.to_date, r.create_date " +
                "FROM reservation r " +
                "JOIN site s USING(site_id) " +
                "JOIN campground c USING(campground_id) " +
                "WHERE c.park_id = ? AND (r.from_date BETWEEN ? AND ? )";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId, LocalDate.now(),LocalDate.now().plusDays(30));

        while(results.next()){
            reservations.add(mapRowToReservation(results));
        }
        return reservations;
    }

    public List<Reservation> openUpcomingReservationsByPark(int parkId){
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT site_id FROM reservation r " +
                "JOIN site s USING(site_id) " +
                "JOIN campground c USING(campground_id) " +
                "WHERE park_id = ? AND (to_date NOT BETWEEN ? and ? ) ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId, LocalDate.now().plusDays(1), LocalDate.now());

        while(results.next()){
            reservations.add(mapRowToReservation(results));
        }
        return reservations;
    }


//TODO

//    public List<Reservation> availableReservationsByPark(int parkId, LocalDate to_date, LocalDate from_date){
//        List<Reservation> reservations = new ArrayList<>();
//        String sql = "SELECT r.reservation_id, r.site_id, r.name, r.from_date, r.to_date, r.create_date " +
//                "FROM reservation r " +
//                "JOIN site s USING(site_id) " +
//                "JOIN campground c USING(campground_id) " +
//                "WHERE c.park_id = ? AND (r.from_date BETWEEN ? AND ? )";
//        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId,to_date,from_date);
//
//        while(results.next()){
//            reservations.add(mapRowToReservation(results));
//        }
//        return reservations;
//    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }


}
