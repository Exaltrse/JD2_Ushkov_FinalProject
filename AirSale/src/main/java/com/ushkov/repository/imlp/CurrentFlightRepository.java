package com.ushkov.repository.imlp;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.repository.CrudOperations;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CurrentFlightRepository implements CrudOperations<Long, CurrentFlight> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<CurrentFlight> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CurrentFlight> currentFlightCriteriaQuery = cb.createQuery(CurrentFlight.class);
            currentFlightCriteriaQuery.select(currentFlightCriteriaQuery.from(CurrentFlight.class));

            return session.createQuery(currentFlightCriteriaQuery).getResultList();
        }
    }

    @Override
    public CurrentFlight findOne(Long id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(CurrentFlight.class, id);
        }
    }

    @Override
    public List<CurrentFlight> findLimitOffset(Long limit, Long offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CurrentFlight> currentFlightCriteriaQuery = cb.createQuery(CurrentFlight.class);
            currentFlightCriteriaQuery.select(currentFlightCriteriaQuery.from(CurrentFlight.class));

            return session.createQuery(currentFlightCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<CurrentFlight> saveAll(List<CurrentFlight> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<CurrentFlight> resultOfSavingCurrentFlightEntitiesList = entities.stream().map(e->e=(CurrentFlight) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingCurrentFlightEntitiesList;
        }
    }

    @Override
    public CurrentFlight saveOne(CurrentFlight entity) {
        try(Session session = sessionFactory.openSession()){
            return (CurrentFlight) session.save(entity);
        }
    }

    @Override
    public CurrentFlight updateOne(CurrentFlight entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
