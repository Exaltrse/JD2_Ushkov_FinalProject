package com.ushkov.repository.springdata;

import com.ushkov.domain.PlaneSeats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PlaneSeatsRepositorySD
        extends CrudRepository<PlaneSeats, Integer>,
        PagingAndSortingRepository<PlaneSeats, Integer>,
        JpaRepository<PlaneSeats, Integer> {
    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update PlaneSeats as a set a.disabled = true where a.id = :id")
    int disableEntity(int id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update PlaneSeats as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Integer> idList);

    Page<PlaneSeats> findAllByDisabledIsFalse(Pageable page);

    List<PlaneSeats> findAllByDisabledIsFalse();
}