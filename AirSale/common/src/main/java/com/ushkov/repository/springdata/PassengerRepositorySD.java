package com.ushkov.repository.springdata;

import com.ushkov.domain.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PassengerRepositorySD
        extends CrudRepository<Passenger, Long>,
        PagingAndSortingRepository<Passenger, Long>,
        JpaRepository<Passenger, Long> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Passenger as a set a.disabled = true where a.id = :id")
    int disableEntity(long id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Passenger as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Long> idList);

    Page<Passenger> findAllByDisabledIsFalse(Pageable page);

    List<Passenger> findAllByDisabledIsFalse();

    Page<Passenger> findAllByIdInAndDisabledIsFalse(List<Long> idList, Pageable page);

    Page<Passenger> findAllByFirstNameIsContainingAndDisabledIsFalse(String name, Pageable page);

    Page<Passenger> findAllByLastNameIsContainingAndDisabledIsFalse(String name, Pageable page);

    Page<Passenger> findByFirstNameIsContainingAndLastNameIsContainingAndDisabledIsFalse(String firstName, String lastName, Pageable page);
}