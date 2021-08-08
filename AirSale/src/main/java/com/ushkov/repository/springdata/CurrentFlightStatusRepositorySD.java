package com.ushkov.repository.springdata;

import com.ushkov.domain.CurrentFlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CurrentFlightStatusRepositorySD
        extends CrudRepository<CurrentFlightStatus, Short>,
        PagingAndSortingRepository<CurrentFlightStatus, Short>,
        JpaRepository<CurrentFlightStatus, Short> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update CurrentFlightStatus as a set a.disabled = true where a.id = :id")
    int disableEntity(short id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update CurrentFlightStatus as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Short> idList);

    Page<CurrentFlightStatus> findAllByDisabledIsFalse(Pageable page);

    List<CurrentFlightStatus> findAllByDisabledIsFalse();
}