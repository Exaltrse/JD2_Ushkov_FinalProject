package com.ushkov.repository;

import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public interface CrudOperations <K, T>{
    //CRUD - create, read, update, without delete operations
    /**
     * Find all entries of DB table.
     *
     * @return      List of entities.
     */
    List<T> findAll();

    /**
     * Find entries from DB table by ID.
     *
     * @param id    ID of entry.
     * @return      Entity of entry.
     */

    T findOne(K id);
    /**
     * Find entries from DB table and return first "limit" entries with "offset".
     *
     * @param limit    Represent how much entries for return.
     * @param offset   Offset from beginning of list of entries.
     * @return         List of entities.
     */
    List<T> findLimitOffset(K limit, K offset);

    /**
     * Save all entities from list of new entities to DB.
     *
     * @param entities List<T> whith new entities.
     * @return         Return List<T> entities of saved entries.
     */
    List<T> saveAll(List<T> entities);

    /**
     * Save one new entity to DB.
     *
     * @param entity   Object with new entity.
     * @return         Return entity of saved entry.
     */
    T saveOne(T entity);

    /**
     * Update one entry in DB by entity. Entry for update choose by entity ID.
     *
     * @param entity   Entity for update.
     * @return         Return entity of updated entry.
     */
    T updateOne(T entity);

}
