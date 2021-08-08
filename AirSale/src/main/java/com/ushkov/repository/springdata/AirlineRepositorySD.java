package com.ushkov.repository.springdata;

import com.ushkov.domain.Airline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface AirlineRepositorySD
        extends CrudRepository<Airline, Short>,
        PagingAndSortingRepository<Airline, Short>,
        JpaRepository<Airline, Short> {

//    //Find operations
//    @Query(value = "select a from Airline as a where a.id = :id")
//    Airline findById(short id);
//
//    @Query(value = "select a from Airline as a where lower(a.name) = lower(:name)")
//    Airline findByName(String name);
//
//    @Query(value = "select a from Airline as a where lower(a.shortName) = lower(:shortname)")
//    Airline findByShortName(String shortname);

//    @Query(value = "select a from Airline as a where a.name like %:name% order by a.name asc")
//    List<Airline> findAllByName(@Param("name") String name, Pageable pageable);
//
//    @Query(value = "select a from Airline  as a where a.shortName like %:shortname% order by a.shortName asc")
//    List<Airline> findAllByShortName(@Param("shortname") String shortname, Pageable pageable);

    //save operations


    //update operations


    //delete operations
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
}
