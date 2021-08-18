package com.ushkov.repository.springdata;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.domain.Passport;
import com.ushkov.domain.Ticket;
import com.ushkov.domain.TicketStatus;

public interface TicketRepositorySD
        extends CrudRepository<Ticket, Long>,
        PagingAndSortingRepository<Ticket, Long>,
        JpaRepository<Ticket, Long> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Ticket as a set a.disabled = true where a.id = :id")
    int disableEntity(long id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Ticket as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Long> idList);

    List<Ticket> findAllByDisabledIsFalse();

    Page<Ticket> findAllByDisabledIsFalse(Pageable page);

    Page<Ticket> findAllByPassport(Passport passport, Pageable pageable);

    Page<Ticket> findAllByCurrentFlight(CurrentFlight currentFlight, Pageable pageable);
    List<Ticket> findAllByCurrentFlight(CurrentFlight currentFlight);

    Page<Ticket> findAllByTicketStatus(TicketStatus entity, Pageable page);

    Page<Ticket> findAllByCurrentFlightAndTicketStatusAndDisabledIsFalse(CurrentFlight currentFlightEntity, TicketStatus ticketStatusEntity, Pageable page);

    List<Ticket> findAllByCurrentFlightAndDisabledIsFalse(CurrentFlight orElseThrow);

    @Query(value = "select t from Ticket as t where t.passport in " +
            "(select psgrpas.passport from PassengerPassport as psgrpas where psgrpas.passenger in " +
            "(select up.passenger from UserPassenger as up where up.user = :id))")
    List<Ticket> findAllByUserId(@Param("id") Integer id);

    @Query(value="select t from Ticket as t where t.currentFlight = :currentflightid and t.ticketStatus in :idlist")
    List<Ticket> findAllByTicketStatusesAndCurrentFlightAndDisableIsFalse(
            @Param("currentflightid") Long currentFlightId,
            @Param("idlist") List<TicketStatus> ticketStatusList);
}