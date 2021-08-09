package com.ushkov.repository.springdata;

import com.ushkov.domain.CurrentFlight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface CurrentFlightRepositorySD
        extends CrudRepository<CurrentFlight, Long>,
        PagingAndSortingRepository<CurrentFlight, Long>,
        JpaRepository<CurrentFlight, Long> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update CurrentFlight as a set a.disabled = true where a.id = :id")
    int disableEntity(long id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update CurrentFlight as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Long> idList);

    Page<CurrentFlight> findAllByDisabledIsFalse(Pageable page);

    List<CurrentFlight> findAllByDisabledIsFalse();

    @Query(value = "select cf from CurrentFlight as cf where " +
            "(cf.departureDate between :departurebeginingdate and :departureenddate) " +
            "and (:currentflightstatusid is null or cf.currentFlightsStatus = :currentflightstatusid) " +
            "and cf.disabled = false")
    Page<CurrentFlight> findAllByDepartureDateBetweenAndDisabledIsFalse(
            @Param("departurebeginingdate") Timestamp departureBeginingDate,
            @Param("departureenddate") Timestamp departureEndDate,
            @Param("currentflightstatusid") Short currentFlightStatusId,
            Pageable page);

    @Query(value = "select cf from CurrentFlight as cf where " +
            "(cf.arrivalDate between :arrivalbeginingdate and :arrivalenddate) " +
            "and (:currentflightstatusid is null or cf.currentFlightsStatus = :currentflightstatusid) " +
            "and cf.disabled = false")
    Page<CurrentFlight> findAllByArrivalDateBetweenAndDisabledIsFalse(
            @Param("arrivalbeginingdate") Timestamp arrivalBeginingDate,
            @Param("arrivalenddate") Timestamp arrivalEndDate,
            @Param("currentflightstatusid") Short currentFlightStatusId,
            Pageable page);

    @Query(value = "select cf from CurrentFlight as cf where " +
            "(cf.departureDate between :departurebeginingdate and :departureenddate) and " +
            "(:currentflightstatusid is null or cf.currentFlightsStatus = :currentflightstatusid) and " +
            "(cf.flight.id in (select f from Flight as f where f.departure = :departureairportid and f.destination = :arrivalairportid))")
    Page<CurrentFlight> findAllByDepartureAndAirports(
            @Param("departurebeginingdate") Timestamp departureBeginingDate,
            @Param("departureenddate") Timestamp departureEndDate,
            @Param("departureairportid") Short departureAirportId,
            @Param("arrivalairportid") Short arrivalAirportId,
            @Param("currentflightstatusid") Short currentFlightStatus,
            Pageable page);
}