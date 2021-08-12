package com.ushkov.repository.springdata;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ushkov.domain.Flight;
import com.ushkov.domain.FlightPlane;
import com.ushkov.domain.Plane;

public interface FlightPlaneRepositorySD
        extends CrudRepository<FlightPlane, Integer>,
        PagingAndSortingRepository<FlightPlane, Integer>,
        JpaRepository<FlightPlane, Integer> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update FlightPlane as a set a.disabled = true where a.id = :id")
    int disableEntity(int id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update FlightPlane as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Integer> idList);

    Page<FlightPlane> findAllByDisabledIsFalse(Pageable page);

    List<FlightPlane> findAllByDisabledIsFalse();

    boolean existsFlightPlaneByFlightAndPlaneAndDisabledIsFalse(Integer flightEntityId, Integer planeEntityId);

    List<FlightPlane> findByFlightAndPlaneAndDisabledIsFalse(Flight flightEntity, Plane planeEntity);

    List<FlightPlane> findAllByPlane(Plane plane);

    List<FlightPlane> findAllByFlight(Flight flight);
}