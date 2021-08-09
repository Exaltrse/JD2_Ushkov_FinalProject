package com.ushkov.repository.springdata;

import com.ushkov.domain.Airline;
import com.ushkov.domain.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TicketStatusRepositorySD
        extends CrudRepository<TicketStatus, Short>,
        PagingAndSortingRepository<TicketStatus, Short>,
        JpaRepository<TicketStatus, Short> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update TicketStatus as a set a.disabled = true where a.id = :id")
    int disableEntity(short id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update TicketStatus as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Short> idList);

    List<TicketStatus> findAllByDisabledIsFalse();

    Page<TicketStatus> findAllByDisabledIsFalse(Pageable page);

    Page<Airline> findAllByNameIsContainingAndDisabledIsFalse(String name, Pageable page);
}