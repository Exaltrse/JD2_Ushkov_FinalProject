package com.ushkov.repository.springdata;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ushkov.domain.Passport;

public interface PassportRepositorySD
        extends CrudRepository<Passport, Long>,
        PagingAndSortingRepository<Passport, Long>,
        JpaRepository<Passport, Long> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Passport as a set a.disabled = true where a.id = :id")
    int disableEntity(long id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Passport as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Long> idList);

    List<Passport> findAllByDisabledIsFalse();

    Page<Passport> findAllByDisabledIsFalse(Pageable page);

    Page<Passport> findAllByFirstNameLatinIsContainingAndDisabledIsFalse(String name, Pageable page);

    Page<Passport> findAllByLastNameLatinIsContainingAndDisabledIsFalse(String name, Pageable page);

    Page<Passport> findByFirstNameLatinIsContainingAndLastNameLatinIsContainingAndDisabledIsFalse(String firstName, String lastName, Pageable page);

    Page<Passport> findAllByIdInAndDisabledIsFalse(List<Long> idList, Pageable page);

    Page<Passport> findAllBySeriesContainingAndDisabledIsFalse(String series, Pageable page);


    @Query(value = "select psp from Passport as psp where psp.id in " +
            "(select psgrpas.passport from PassengerPassport as psgrpas where psgrpas.passenger in " +
            "(select up.passenger from UserPassenger as up where up.user = :id))")
    List<Passport> findPassportByUserId(Integer id);

    @Query(value = "select psp from Passport as psp where psp.id in " +
            "(select psgrpas.passport from PassengerPassport as psgrpas where psgrpas.passenger in " +
            "(select up.passenger from UserPassenger as up where up.user = :id))")
    Page<Passport> findPassportByUserId(Integer id, Pageable page);
}