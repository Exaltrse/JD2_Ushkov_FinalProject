package com.ushkov.repository.springdata;

import com.ushkov.domain.CurrentFlight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

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
}