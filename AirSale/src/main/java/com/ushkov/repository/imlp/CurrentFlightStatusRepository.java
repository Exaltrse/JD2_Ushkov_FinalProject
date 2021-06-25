package com.ushkov.repository.imlp;

import com.ushkov.domain.CurrentFlightStatus;
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
public class CurrentFlightStatusRepository implements CrudOperations<Short, CurrentFlightStatus> {
    @Autowired
    @Qualifier("sessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public List<CurrentFlightStatus> findAll() {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CurrentFlightStatus> currentFlightStatusCriteriaQuery = cb.createQuery(CurrentFlightStatus.class);
            currentFlightStatusCriteriaQuery.select(currentFlightStatusCriteriaQuery.from(CurrentFlightStatus.class));

            return session.createQuery(currentFlightStatusCriteriaQuery).getResultList();
        }
    }

    @Override
    public CurrentFlightStatus findOne(Short id) {
        try(Session session = sessionFactory.openSession()){
            return session.find(CurrentFlightStatus.class, id);
        }
    }

    @Override
    public List<CurrentFlightStatus> findLimitOffset(Short limit, Short offset) {
        try(Session session = sessionFactory.openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<CurrentFlightStatus> currentFlightStatusCriteriaQuery = cb.createQuery(CurrentFlightStatus.class);
            currentFlightStatusCriteriaQuery.select(currentFlightStatusCriteriaQuery.from(CurrentFlightStatus.class));

            return session.createQuery(currentFlightStatusCriteriaQuery).setFirstResult(offset.intValue()).setMaxResults(limit.intValue()).getResultList();
        }
    }

    @Override
    public List<CurrentFlightStatus> saveAll(List<CurrentFlightStatus> entities) {
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            List<CurrentFlightStatus> resultOfSavingCurrentFlightStatusEntitiesList = entities.stream().map(e->e=(CurrentFlightStatus) session.save(e)).collect(Collectors.toList());
            session.getTransaction().commit();
            return resultOfSavingCurrentFlightStatusEntitiesList;
        }
    }

    @Override
    public CurrentFlightStatus saveOne(CurrentFlightStatus entity) {
        try(Session session = sessionFactory.openSession()){
            return (CurrentFlightStatus) session.save(entity);
        }
    }

    @Override
    public CurrentFlightStatus updateOne(CurrentFlightStatus entity) {
        try(Session session = sessionFactory.openSession()){
            session.update(entity);
            return findOne(entity.getId());
        }
    }
}
