package com.ushkov.repository.springdata;

import com.ushkov.domain.Role;
import com.ushkov.domain.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface UsersRepositorySD
        extends CrudRepository<Users, Integer>,
        PagingAndSortingRepository<Users, Integer>,
        JpaRepository<Users, Integer> {

    //"delete" operations
    @Modifying(flushAutomatically = true)
    @Query(value = "update Users as a set a.disabled = true where a.id = :id")
    int disableEntity(int id);

    @Modifying(flushAutomatically = true)
    @Query(value = "update Users as a set a.disabled = true where a.id in :idList")
    int disableEntities(List<Integer> idList);

    List<Users> findAllByDisabledIsFalse();

    Page<Users> findAllByDisabledIsFalse(Pageable page);

    Page<Users> findAllByLoginIsContainingAndDisabledIsFalse(String login, Pageable page);

    Users findByLogin(String login);

    Page<Users> findAllByRole(Role role, Pageable pageable);
}