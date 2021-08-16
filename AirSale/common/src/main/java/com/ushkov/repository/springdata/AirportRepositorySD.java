package com.ushkov.repository.springdata;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ushkov.domain.Airport;

public interface AirportRepositorySD
        extends CrudRepository<Airport, Short>,
        PagingAndSortingRepository<Airport, Short>,
        JpaRepository<Airport, Short> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Airport as a set a.disabled = true where a.id = :id")
    int disableEntity(short id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Airport as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Short> idList);

    List<Airport> findAllByDisabledIsFalse();

    Page<Airport> findAllByDisabledIsFalse(Pageable page);

    Page<Airport> findAllByNameIsContainingAndDisabledIsFalse(String name, Pageable page);

    Page<Airport> findAllByShortNameIsContainingAndDisabledIsFalse(String name, Pageable page);
}
