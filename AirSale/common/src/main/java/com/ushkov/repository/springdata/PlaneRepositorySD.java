package com.ushkov.repository.springdata;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ushkov.domain.Plane;

public interface PlaneRepositorySD
        extends CrudRepository<Plane, Integer>,
        PagingAndSortingRepository<Plane, Integer>,
        JpaRepository<Plane, Integer> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Plane as a set a.disabled = true where a.id = :id")
    int disableEntity(int id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Plane as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Integer> idList);

    Page<Plane> findAllByDisabledIsFalse(Pageable page);

    List<Plane> findAllByDisabledIsFalse();

    Page<Plane> findAllByAircraftNumberIsContainingAndDisabledIsFalse(String name, Pageable page);

   Plane findByAircraftNumber(String aircraftnumber);
}