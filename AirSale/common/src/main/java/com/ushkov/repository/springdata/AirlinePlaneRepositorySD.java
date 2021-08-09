package com.ushkov.repository.springdata;

import com.ushkov.domain.AirlinePlane;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AirlinePlaneRepositorySD
        extends CrudRepository<AirlinePlane, Long>,
        PagingAndSortingRepository<AirlinePlane, Long>,
        JpaRepository<AirlinePlane, Long> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update AirlinePlane as a set a.disabled = true where a.id = :id")
    int disableEntity(long id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update AirlinePlane as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Long> idList);

    Page<AirlinePlane> findAllByDisabledIsFalse(Pageable page);

    List<AirlinePlane> findAllByDisabledIsFalse();

    boolean existsAirlinePlaneByAirlineAndPlaneAndDisabledIsFalse(short airline, long plane);

    List<AirlinePlane> findByAirlineAndPlaneAndDisabledIsFalse(short airline, long plane);
}
