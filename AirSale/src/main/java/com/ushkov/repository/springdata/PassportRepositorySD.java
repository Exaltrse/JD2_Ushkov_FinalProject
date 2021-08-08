package com.ushkov.repository.springdata;

import com.ushkov.domain.Passport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

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
}