package com.ushkov.repository.springdata;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ushkov.domain.PassengerClass;

public interface PassengerClassRepositorySD
        extends CrudRepository<PassengerClass, Short>,
        PagingAndSortingRepository<PassengerClass, Short>,
        JpaRepository<PassengerClass, Short> {

    //delete operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update PassengerClass as a set a.disabled = true where a.id = :id")
    int disableEntity(short id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update PassengerClass as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Short> idList);

    Page<PassengerClass> findAllByDisabledIsFalse(Pageable page);

    List<PassengerClass> findAllByDisabledIsFalse();

    Page<PassengerClass> findAllByNameIsContainingAndDisabledIsFalse(String name, Pageable page);
}