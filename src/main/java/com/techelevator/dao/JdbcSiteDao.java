package com.techelevator.dao;

import com.techelevator.model.Park;
import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        // List all sites in a park(all campgrounds) that allow RVs
        List<Site> sites = new ArrayList<>();
        String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities " +
                "FROM site s " +
                "JOIN campground c USING(campground_id) " +
                "WHERE park_id = ? AND (max_rv_length > 0) ";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);

        while(results.next()){
            sites.add(mapRowToSite(results));
        }

        return sites;
    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }
}
