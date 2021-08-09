package com.ushkov.repository.springdata;

import com.ushkov.domain.Airport;
import com.ushkov.domain.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FlightRepositorySD
        extends CrudRepository<Flight, Integer>,
        PagingAndSortingRepository<Flight, Integer>,
        JpaRepository<Flight, Integer> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Flight as a set a.disabled = true where a.id = :id")
    int disableEntity(int id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Flight as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Integer> idList);

    List<Flight> findAllByDisabledIsFalse();

    Page<Flight> findAllByDisabledIsFalse(Pageable page);

    Page<Flight> findAllByNumberIsContainingAndDisabledIsFalse(String flightnumber, Pageable page);

    Page<Flight> findAllByDepartureAndDestinationAndDisabledIsFalse(Airport departureAirportId, Airport arrivalAirportId, Pageable page);
}