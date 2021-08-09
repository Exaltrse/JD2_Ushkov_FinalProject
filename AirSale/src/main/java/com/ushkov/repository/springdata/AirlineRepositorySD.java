package com.ushkov.repository.springdata;

import com.ushkov.domain.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AirlineRepositorySD
        extends CrudRepository<Airline, Short>,
        PagingAndSortingRepository<Airline, Short>,
        JpaRepository<Airline, Short> {

    @Modifying(flushAutomatically = true)
    @Query(value = "update Airline as a set a.disabled = true where a.id = :id")
    int disableEntity(short id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Airline as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Short> idList);



    Page<Airline> findAllByNameIsContainingAndDisabledIsFalse(String name, Pageable page);

    Page<Airline> findAllByShortNameIsContainingAndDisabledIsFalse(String name, Pageable page);

    List<Airline> findAllByDisabledIsFalse();

    Page<Airline> findAllByDisabledIsFalse(Pageable page);

    @Query(value = "select a from Airline as a where a.id in (select ap from AirlinePlane as ap where ap.plane in :collect) and a.disabled = false")
    Page<Airline> findAllByPlaneAndDisabledIsFalse(@Param("collect") List<Integer> collect, Pageable page);
}
